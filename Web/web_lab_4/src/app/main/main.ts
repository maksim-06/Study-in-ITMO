import {AfterViewInit, ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {Auth} from '../services/auth';
import {Point} from '../services/point';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-main',
  imports: [FormsModule, CommonModule],
  templateUrl: './main.html',
  styleUrl: './main.css',
})
export class Main implements OnInit, AfterViewInit {
  @ViewChild('graph') graph: any;

  x: string = "";
  y: string = "";
  r: string = "1";
  points: any[] = [];
  errormessage: string = "";

  constructor(
    private router: Router,
    private auth: Auth,
    private point: Point,
    private cdr: ChangeDetectorRef) {
  }

  ngAfterViewInit() {
    this.drawGraph();
  }

  ngOnInit() {
    this.loadUserPoints();
  }


  private loadUserPoints() {
    this.point.getUserPoints().subscribe({
      next: (points: any[]) => {
        this.points = points;
        this.cdr.detectChanges();
        this.drawGraph();
      },
      error: (error) => {
        if (error.status === 401) {
          this.router.navigate(['/login']);
        } else{
          console.error('Error loading points:', error);
        }
      }
    });
  }


  logout() {
    this.auth.logout().subscribe({
      next: () => {
        this.router.navigate(["/login"]);
      },
      error: (error) => {
        this.router.navigate(["/login"]);
      }
    })

  }

  onRChange() {
    this.drawGraph();
  }

  onGraphClick(event: MouseEvent) {
    const svg = document.getElementById('graph');
    if (!svg) return;

    const rect = svg.getBoundingClientRect();
    const clickX = event.clientX - rect.left;
    const clickY = event.clientY - rect.top;

    const currentR = parseFloat(this.r) || 1;

    const mathX = ((clickX - 150) / 100) * currentR;
    const mathY = ((150 - clickY) / 100) * currentR;

    this.x = mathX.toString();
    this.y = mathY.toString();

    this.checkPoint();
  }

  checkPoint() {
    this.x = this.x.replace(',', '.');
    this.y = this.y.replace(',', '.');
    this.r = this.r.replace(',', '.');

    if (!this.validateInput()) {
      return;
    }

    this.point.checkPoint(this.x, this.y, this.r).subscribe({
      next: (response: any) => {
        this.points.unshift(response);
        this.errormessage = "";
        this.cdr.detectChanges();
        this.drawGraph();
      },
      error: (error) => {
        this.errormessage = error.error.error;
      }
    });
  }

  private validateInput() {
    var xParam = parseFloat(this.x);
    var yParam = parseFloat(this.y);
    var rParam = parseFloat(this.r);

    if (isNaN(xParam) || isNaN(yParam) || isNaN(rParam)) {
      this.errormessage = 'Enter the numbers';
      return false;
    }

    if (xParam < -3 || xParam > 3) {
      this.errormessage = "X should be between -3 and 3";
      return false;
    }

    if (yParam < -3 || yParam > 5) {
      this.errormessage = "Y should be between -3 and 5";
      return false;
    }


    if (rParam <= 0 || rParam > 3) {
      this.errormessage = "R should be between 0 and 3";
      return false;
    }

    return true;
  }

  // -----------------------------
  private drawGraph() {
    const graph = document.getElementById("graph");

    if (!graph) return;

    const pointsToRemove = graph.querySelectorAll('.graph-point');
    pointsToRemove.forEach(point => {
      point.remove();
    });

    this.points.forEach(point => {
      this.addPointToGraph(point.x, point.y, point.r, point.isShoot);
    });

    this.updateLabels();

  }

  private updateLabels() {
    const currentR = parseFloat(this.r) || 1;
    const halfR = (currentR / 2).toFixed(1);


    const numHalfR = parseFloat(halfR);
    const numCurrentR = currentR;

    const x194 = document.querySelector('text[x="194"]');
    if (x194) x194.textContent = halfR;

    const x244 = document.querySelector('text[x="244"]');
    if (x244) x244.textContent = currentR.toString();

    const x106 = document.querySelector('text[x="106"]');
    if (x106) x106.textContent = (-numHalfR).toString();

    const x42 = document.querySelector('text[x="42"]');
    if (x42) x42.textContent = (-numCurrentR).toString();

    const y106 = document.querySelector('text[y="106"]');
    if (y106) y106.textContent = halfR;

    const y56 = document.querySelector('text[y="56"]');
    if (y56) y56.textContent = currentR.toString();

    const y194 = document.querySelector('text[y="194"]');
    if (y194) y194.textContent = (-numHalfR).toString();

    const y244 = document.querySelector('text[y="244"]');
    if (y244) y244.textContent = (-numCurrentR).toString();
  }

  private addPointToGraph(x: number, y: number, r: number, isShoot: any) {
    const currentR = parseFloat(this.r) || 1;

    if (Math.abs(currentR - r) > 0.01) {
      return;
    }

    const svgX = 150 + (x / currentR) * 100;
    const svgY = 150 - (y / currentR) * 100;

    const point = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
    point.setAttribute("cx", svgX.toString());
    point.setAttribute("cy", svgY.toString());
    point.setAttribute("r", "3");

    if (isShoot === 'Попал') {
      point.setAttribute('fill', '#4CAF50');
      point.setAttribute('stroke', '#2E7D32');
    } else {
      point.setAttribute('fill', '#F44336');
      point.setAttribute('stroke', '#C62828');
    }

    point.setAttribute('stroke-width', '1');
    point.setAttribute('class', 'graph-point');

    const graph = document.getElementById("graph");
    if (graph) {
      graph.appendChild(point);
    }
  }
}

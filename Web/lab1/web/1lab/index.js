"use strict";

const state = {
    x: 0,
    y: 0.0,
    r: 1.0,
};

const table = document.getElementById("result-table");
const error = document.getElementById("err");
const possibleXs = new Set([-2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2]);
const possibleRs = new Set([1, 1.5, 2, 2.5, 3]);



const validate = (state) => {
    const xNum = parseFloat(state.x);
    if (!possibleXs.has(xNum)) {
        error.hidden = false;
        error.innerText = `x must be one of: ${[...possibleXs].join(", ")}`;
        throw new Error("invalid state");
    }


    const yNum = parseFloat(state.y);

    if (isNaN(yNum)) {
        error.hidden = false;
        error.innerText = "y must be a valid number";
        throw new Error("invalid state");
    }

    if (yNum < -3.0 || yNum > 5.0) {
        error.hidden = false;
        error.innerText = "y must be float in range {-3 ... 5}";
        throw new Error("invalid state");
    }



    const rNum = parseFloat(state.r);
    if (!possibleRs.has(rNum)) {
        error.hidden = false;
        error.innerText = `r must be one of: ${[...possibleRs].join(", ")}`;
        throw new Error("invalid state");
    }

    error.hidden = true;
}

let selectedBtn = null;

const xsContainer = document.getElementById("xs");
if (xsContainer) {
    xsContainer.addEventListener("click", function (ev) {
        if (ev.target.tagName === "INPUT") {
            const clickedBtn = ev.target;

            if (selectedBtn != null) {
                selectedBtn.style.border = "";
            }

            selectedBtn = clickedBtn;
            state.x = ev.target.value;
            selectedBtn.style.border = "#FF6961 2px solid";
        }
    });
}

const yInput = document.getElementById("y");
if (yInput) {
    yInput.addEventListener("change", (ev) => {
        state.y = ev.target.value;
    });
}

const rInput = document.getElementById("r");
if (rInput) {
    rInput.addEventListener("change", (ev) => {
        state.r = ev.target.value;
    });
}

const dataForm = document.getElementById("data-form");
if (dataForm) {
    dataForm.addEventListener("submit", async function (ev) {
        ev.preventDefault();

        try {
            validate(state);

            const params = new URLSearchParams();
            params.append("x", state.x);
            params.append("y", state.y);
            params.append("r", state.r);

            try {
                const response = await fetch("/fcgi-bin/server.jar?" + params.toString());

                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }

                const result = await response.json();


                const prevResults = JSON.parse(localStorage.getItem("results") || "[]");
                const newResult = {
                    isShoot: result.isShoot,
                    x: result.x,
                    y: result.y,
                    r: result.r,
                    time: result.time,
                    execTime: result.execTime
                };

                localStorage.setItem("results", JSON.stringify([...prevResults, newResult]));


                addRowToTable(newResult);

            } catch (error) {
                console.error('Request failed:', error);
                const errorResult = {
                    isShoot: "Ошибка",
                    x: state.x,
                    y: state.y,
                    r: state.r,
                    time: new Date().toLocaleString(),
                    execTime: "N/A"
                };
                addRowToTable(errorResult);
            }
        } catch (validationError) {
            console.error('Validation failed:', validationError);
        }
    });
}

function addRowToTable(result) {
    const newRow = table.insertRow(-1);

    const isShoot = newRow.insertCell(0);
    const rowX = newRow.insertCell(1);
    const rowY = newRow.insertCell(2);
    const rowR = newRow.insertCell(3);
    const rowTime = newRow.insertCell(4);
    const rowExecTime = newRow.insertCell(5);

    isShoot.innerText = result.isShoot;
    rowX.innerText = result.x;
    rowY.innerText = result.y;
    rowR.innerText = result.r;
    rowTime.innerText = result.time;
    rowExecTime.innerText = result.execTime;
}



// async function loadHistory() {
//     try {
//         let prevResults = JSON.parse(localStorage.getItem("results") || "[]");
//
//
//         if (!prevResults || prevResults.length === 0) {
//             try {
//                 const response = await fetch("/fcgi-bin/server.jar/history");
//                 if (response.ok) {
//                     const serverHistory = await response.json();
//                     localStorage.setItem("results", JSON.stringify(serverHistory));
//                     prevResults = serverHistory;
//                 }
//             } catch (fetchError) {
//                 console.error('Error loading history from server:', fetchError);
//             }
//         }
//
//
//         prevResults.forEach(result => {
//             addRowToTable(result);
//         });
//     } catch (e) {
//         console.error('Error loading previous results:', e);
//         localStorage.setItem("results", "[]");
//     }
// }


async function loadHistory() {
    console.log("=== НАЧАЛО ЗАГРУЗКИ ИСТОРИИ ===");

    try {
        let prevResults = JSON.parse(localStorage.getItem("results") || "[]");
        console.log("📦 LocalStorage до загрузки:", prevResults);
        console.log("Количество записей в LocalStorage:", prevResults.length);

        if (!prevResults || prevResults.length === 0) {
            console.log("🔄 LocalStorage пуст, загружаем с сервера...");
            try {
                const response = await fetch("/fcgi-bin/server.jar/history");
                console.log("📡 Ответ сервера:", response.status, response.statusText);

                if (response.ok) {
                    const serverHistory = await response.json();
                    console.log("✅ Данные с сервера:", serverHistory);
                    console.log("Количество записей с сервера:", serverHistory.length);

                    localStorage.setItem("results", JSON.stringify(serverHistory));
                    prevResults = serverHistory;

                    console.log("💾 LocalStorage после обновления:",
                        JSON.parse(localStorage.getItem("results")));
                } else {
                    console.error("❌ Ошибка сервера:", response.status);
                }
            } catch (fetchError) {
                console.error('❌ Ошибка загрузки с сервера:', fetchError);
            }
        } else {
            console.log("📚 Используем данные из LocalStorage");
        }

        // Очищаем таблицу перед добавлением
        while (table.rows.length > 1) {
            table.deleteRow(1);
        }

        // Добавляем данные в таблицу
        prevResults.forEach((result, index) => {
            console.log(`📝 Добавляем запись ${index + 1}:`, result);
            addRowToTable(result);
        });

        console.log("=== ЗАВЕРШЕНИЕ ЗАГРУЗКИ ИСТОРИИ ===");

    } catch (e) {
        console.error('❌ Ошибка загрузки предыдущих результатов:', e);
        localStorage.setItem("results", "[]");
    }
}



document.addEventListener('DOMContentLoaded', function() {
    loadHistory();
});
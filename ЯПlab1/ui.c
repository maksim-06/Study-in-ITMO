#include "ui.h"
#include <stdio.h>

void ui_print_menu(void) {
    printf("\n=== CONTACT LIST ===\n");
    printf("1. Print all contacts\n");
    printf("2. Search for a contact\n");
    printf("3. Add new contact\n");
    printf("4. Exit\n");
    printf("Choose action: ");
}


void ui_show_all_contacts(ContactBST* tree) {
    if (tree == NULL || tree->root==NULL) {
        printf("The phone book is empty\n");
        return;
    }

    printf("\n--- All Contacts ---\n");
    bst_inorder(tree, contact_print);
}


void ui_search_contact(ContactBST* tree) {
    if (tree == NULL) {
        printf("Error: the tree is not initialized\n");
        return;
    }

    int choice;
    printf("\n=== CONTACT SEARCH ===\n");
    printf("1. Search by name\n");
    printf("2. Search by surname\n");
    printf("3. Search by name and surname\n");
    printf("4. Search by phone\n");
    printf("5. Search by email\n");
    printf("Choose search type: ");
    scanf("%d", &choice);
    getchar();

    if (choice == 1) {
        char name[MAX_NAME];
        printf("Enter name: ");
        scanf("%49s", name);
        printf("\nSearch results:\n");

        bst_search_all_fields(tree,name, contact_print);
    } else if (choice == 2) {
        char surname[MAX_SURNAME];
        printf("Enter surname: ");
        scanf("%49s", surname);
        printf("\nSearch results:\n");
        bst_search_all_fields(tree,surname, contact_print);
    } else if (choice == 3) {
        char name[MAX_NAME], surname[MAX_NAME];
        printf("Enter name: ");
        scanf("%49s", name);
        printf("Enter surname: ");
        scanf("%49s", surname);

        printf("\nSearch results:\n");
        bst_search_by_name_and_surname_wrapper(tree, name, surname, contact_print);
    } else if (choice == 4) {
        char phone[MAX_PHONE];
        printf("Enter phone: ");
        scanf("%49s", phone);
        printf("\nSearch results:\n");
        bst_search_all_fields(tree, phone, contact_print);
    } else if (choice == 5) {
        char email[MAX_EMAIL];
        printf("Enter email: ");
        scanf("%49s", email);
        printf("\nSearch results:\n");
        bst_search_all_fields(tree, email, contact_print);
    }
}

void ui_add_contact(ContactBST* tree) {
    if (tree == NULL) {
        printf("Error: the tree is not initialized\n");
        return;
    }

    char name[MAX_NAME], surname[MAX_SURNAME], phone[MAX_PHONE], email[MAX_EMAIL];

    printf("Enter name: ");
    scanf("%49s", name);
    printf("Enter surname: ");
    scanf("%49s", surname);
    printf("Enter phone: ");
    scanf("%49s", phone);
    printf("Enter email: ");
    scanf("%49s", email);

    Contact new_contact = contact_create(name, surname, phone, email);

    if (bst_insert(tree, new_contact)) {
        printf("Contact added successfully!\n");
    } else {
        printf("Error: failed to add contact\n");
    }
}

void ui_run(ContactBST* tree) {
    if (tree == NULL) {
        printf("Error: tree not initialized\n");
        return;
    }

    int choice;

    do {
        ui_print_menu();
        scanf("%d", &choice);

        switch (choice) {
            case 1:
                ui_show_all_contacts(tree);
                break;
            case 2:
                ui_search_contact(tree);
                break;
            case 3:
                ui_add_contact(tree);
                break;
            case 4:
                printf("Exit...\n");
                break;
            default:
                printf("Invalid choice!\n");
        }
    } while (choice != 4);
}







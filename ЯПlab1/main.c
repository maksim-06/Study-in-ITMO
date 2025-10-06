#include <stdio.h>

#include "bst.h"
#include "ui.h"




int main(void) {
    ContactBST* contact_tree = bst_create();
    if (!contact_tree) {
        printf("Failed to create contact tree\n");
        return 1;
    }

    ui_run(contact_tree);

    bst_destroy(contact_tree);
    return 0;
}
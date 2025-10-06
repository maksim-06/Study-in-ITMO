#include "bst.h"
#include <stdlib.h>
#include <string.h>


ContactBST *bst_create(void) {
    ContactBST *tree = malloc(sizeof(ContactBST));
    if (tree) {
        tree->root = NULL;
        tree->size = 0;
    }
    return tree;
}


void bst_help_destroy(BSTNode* node) {
    if (node == NULL) return;
    bst_help_destroy(node->left);
    bst_help_destroy(node->right);
    free(node);
}

void bst_destroy(ContactBST* tree) {
    if (tree == NULL) return;
    bst_help_destroy(tree->root);
    free(tree);
}

int bst_insert(ContactBST* tree, Contact contact) {
    if (tree == NULL) return 0;

    BSTNode* new_node = malloc(sizeof(BSTNode));
    if (new_node == NULL) return 0;

    new_node->contact = contact;
    new_node->left = NULL;
    new_node->right=NULL;

    if (tree -> root == NULL) {
        tree->root = new_node;
        tree->size = 1;
        return 1;
    }

    BSTNode* current = tree -> root;

    while (1) {
        int cmp = compare_contacts(&contact, &current->contact);

        if (cmp<0) {
            if (current->left == NULL) {
                current->left = new_node;
                break;
            }
            current = current->left;
        }

        else if (cmp > 0) {
            if (current->right==NULL) {
                current->right = new_node;
                break;
            }
            current = current->right;
        }

        else {
            free(new_node);
            return 0;
        }
    }
    tree->size++;
    return 1;
}
void bst_inorder_help(BSTNode* node, void (*action)(Contact)) {
    if (node == NULL) return;

    bst_inorder_help(node->left, action);

    action(node->contact);

    bst_inorder_help(node->right,action);
}

void bst_inorder(ContactBST* tree, void (*action)(Contact)) {
    if (tree == NULL || action == NULL) return;

    bst_inorder_help(tree->root,action);

}

BSTNode* bst_search_by_name_help(BSTNode* node, const char* name) {
    if (node == NULL) return NULL;

    int cmp = strcmp(name, node->contact.name);

    if (cmp == 0) return node;
    if (cmp<0) return bst_search_by_name_help(node->left, name);
    return bst_search_by_name_help(node->right,name);
}

BSTNode* bst_search_by_name(ContactBST* tree, const char* name) {
    if (tree == NULL || name == NULL) return NULL;
    return bst_search_by_name_help(tree->root,name);
}



void bst_search_all_fields_help(BSTNode* node, const char* search_term, void (*action) (Contact)) {
    if (node==NULL) return;

    if (strstr(node->contact.name, search_term) != NULL ||
        strstr(node->contact.surname, search_term) != NULL ||
        strstr(node->contact.phone, search_term) != NULL ||
        strstr(node->contact.email, search_term) != NULL) {
        action(node->contact);
    }

    bst_search_all_fields_help(node->left, search_term,action);
    bst_search_all_fields_help(node->right,search_term,action);
}

void bst_search_all_fields(ContactBST* tree, const char* search_term, void (*action) (Contact)) {
    if (tree == NULL || search_term==NULL || action == NULL) return;
    bst_search_all_fields_help(tree->root,search_term,action);
}


void bst_search_by_name_and_surname(BSTNode* node, const char* name, const char* surname, void (*action)(Contact)) {
    if (node == NULL) return;

    if (strcmp(node->contact.name, name) == 0 && strcmp(node->contact.surname, surname) == 0) {
        action(node->contact);
    }

    bst_search_by_name_and_surname(node->left, name, surname, action);
    bst_search_by_name_and_surname(node->right, name, surname, action);
}

void bst_search_by_name_and_surname_wrapper(ContactBST* tree, const char* name, const char* surname, void (*action)(Contact)) {
    if (tree == NULL || name == NULL || surname == NULL || action == NULL) return;
    bst_search_by_name_and_surname(tree->root, name, surname, action);
}


int compare_contacts(const Contact* a, const Contact* b) {
    int name_cmp = strcmp(a->name, b->name);
    if (name_cmp != 0) return name_cmp;
    return strcmp(a->surname, b->surname);
}
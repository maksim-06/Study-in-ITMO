#ifndef BST_H
#define BST_H



#include "contact.h"

typedef struct BSTNode {
    Contact contact;
    struct BSTNode* left;
    struct BSTNode* right;
} BSTNode;


typedef struct {
    BSTNode* root;
    int size;
} ContactBST;

ContactBST* bst_create(void);
void bst_destroy(ContactBST* tree);
int bst_insert(ContactBST* tree, Contact contact);
void bst_inorder(ContactBST* tree, void (*action)(Contact));

BSTNode* bst_search_by_name(ContactBST* tree, const char* name);
void bst_search_all_fields(ContactBST* tree, const char* search_term, void (*action) (Contact));
void bst_search_by_name_and_surname_wrapper(ContactBST* tree, const char* name, const char* surname, void (*action)(Contact));
int compare_contacts(const Contact* a, const Contact* b);

#endif

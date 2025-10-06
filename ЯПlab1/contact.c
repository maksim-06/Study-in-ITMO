#include "contact.h"
#include <string.h>
#include <stdio.h>


Contact contact_create(const char* name, const char* surname,const char* phone, const char* email) {
    Contact c;
    strncpy(c.name, name, MAX_NAME-1);
    strncpy(c.surname, surname, MAX_NAME-1);
    strncpy(c.phone, phone, MAX_PHONE-1);
    strncpy(c.email, email,MAX_EMAIL-1);
    return c;
}

void contact_print(Contact contact) {
    printf("%s | %s | %s | %s\n", contact.name, contact.surname, contact.phone, contact.email);
}

int contact_contains(Contact contact, const char* search_term) {
    if (strstr(contact.name, search_term) != NULL) return 1;
    if (strstr(contact.surname, search_term) != NULL) return 1;
    if (strstr(contact.phone, search_term) != NULL) return 1;
    if (strstr(contact.email,search_term) != NULL) return 1;
    return 0;
}



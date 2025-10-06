#ifndef LAB1_CONTACT_H
#define LAB1_CONTACT_H

#define MAX_NAME 50
#define MAX_SURNAME 50
#define MAX_PHONE 50
#define MAX_EMAIL 50

typedef struct {
    char name[MAX_NAME];
    char surname[MAX_SURNAME];
    char phone[MAX_PHONE];
    char email[MAX_EMAIL];
} Contact;

Contact contact_create(const char* name, const char* surname,const char *phone, const char *email);

void contact_print(Contact contact);

int contact_contains(Contact contact, const char *search_term);


#endif //LAB1_CONTACT_H

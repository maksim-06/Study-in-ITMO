using System;

namespace ConsoleApplication2
{
    public class Contact
    {
        public const int MAX_NAME = 50;
        public const int MAX_SURNAME = 50;
        public const int MAX_PHONE = 20;
        public const int MAX_EMAIL = 50;

        public string Name { get; set; }
        public string Surname { get; set; }
        public string Phone { get; set; }
        public string Email { get; set; }


        public static Contact Create(string name, string surname, string phone, string email)
        {
            var contact = new Contact();

            if (name != null && name.Length > MAX_NAME)
                contact.Name = name.Substring(0, MAX_NAME);
            else
                contact.Name = name;

            if (surname != null && surname.Length > MAX_SURNAME)
                contact.Surname = surname.Substring(0, MAX_SURNAME);
            else
                contact.Surname = surname;

            if (phone != null && phone.Length > MAX_PHONE)
                contact.Phone = phone.Substring(0, MAX_PHONE);
            else
                contact.Phone = phone;

            if (email != null && email.Length > MAX_EMAIL)
                contact.Email = email.Substring(0, MAX_EMAIL);
            else
                contact.Email = email;

            return contact;
        }

        public void Print()
        {
            Console.WriteLine($"{Name} | {Surname} | {Phone} | {Email}");
        }

        public bool Contains(string search)
        {
            if (string.IsNullOrEmpty(search)) return false;

            return (Name != null && Name.Contains(search)) ||
                   (Surname != null && Surname.Contains(search)) ||
                   (Phone != null && Phone.Contains(search)) ||
                   (Email != null && Email.Contains(search));
        }
    }
}
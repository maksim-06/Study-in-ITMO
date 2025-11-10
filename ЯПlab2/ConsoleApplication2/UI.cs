using System;

namespace ConsoleApplication2
{
    public class UI
    {
        public static void PrintMenu()
        {
            Console.WriteLine("\n=== CONTACT LIST ===");
            Console.WriteLine("1. Print all contacts");
            Console.WriteLine("2. Search for a contact");
            Console.WriteLine("3. Add new contact");
            Console.WriteLine("4. Exit");
            Console.Write("Choose action: ");
        }

        public static void ShowAllContacts(ContactBST tree)
        {
            if (tree == null)
            {
                Console.WriteLine("The phone book is empty");
                return;
            }

            Console.WriteLine("\n--- All Contacts ---");
            tree.Inorder(contact => contact.Print());
        }


        public static void SearchContact(ContactBST tree)
        {
            if (tree == null)
            {
                Console.WriteLine("Error: the tree is not initialized");
                return;
            }

            Console.WriteLine("\n=== CONTACT SEARCH ===");
            Console.WriteLine("1. Search by name");
            Console.WriteLine("2. Search by surname");
            Console.WriteLine("3. Search by name and surname");
            Console.WriteLine("4. Search by phone");
            Console.WriteLine("5. Search by email");
            Console.Write("Choose search type: ");

            if (!int.TryParse(Console.ReadLine(), out int choice))
            {
                Console.WriteLine("Invalid input!");
                return;
            }

            switch (choice)
            {
                case 1:
                    Console.Write("Enter name: ");
                    string name = Console.ReadLine();
                    Console.WriteLine("\nSearch results:");
                    tree.SearchAllFields(name, contact => contact.Print());
                    break;

                case 2:
                    Console.Write("Enter surname: ");
                    string surname = Console.ReadLine();
                    Console.WriteLine("\nSearch results:");
                    tree.SearchAllFields(surname, contact => contact.Print());
                    break;

                case 3:
                    Console.Write("Enter name: ");
                    string name3 = Console.ReadLine();
                    Console.Write("Enter surname: ");
                    string surname3 = Console.ReadLine();
                    Console.WriteLine("\nSearch results:");
                    tree.SearchByNameAndSurname(name3, surname3, contact => contact.Print());
                    break;

                case 4:
                    Console.Write("Enter phone: ");
                    string phone = Console.ReadLine();
                    Console.WriteLine("\nSearch results:");
                    tree.SearchAllFields(phone, contact => contact.Print());
                    break;

                case 5:
                    Console.Write("Enter email: ");
                    string email = Console.ReadLine();
                    Console.WriteLine("\nSearch results:");
                    tree.SearchAllFields(email, contact => contact.Print());
                    break;

                default:
                    Console.WriteLine("Invalid choice!");
                    break;
            }
        }

        public static void AddContact(ContactBST tree)
        {
            if (tree == null)
            {
                Console.WriteLine("Error: the tree is not initialized");
                return;
            }

            Console.Write("Enter name: ");
            string name = Console.ReadLine();
            Console.Write("Enter surname: ");
            string surname = Console.ReadLine();
            Console.Write("Enter phone: ");
            string phone = Console.ReadLine();
            Console.Write("Enter email: ");
            string email = Console.ReadLine();

            Contact newContact = Contact.Create(name, surname, phone, email);

            if (tree.Insert(newContact))
            {
                Console.WriteLine("Contact added successfully!");
                FileStorage.Save(tree);
            }
            else
            {
                Console.WriteLine("Error: failed to add contact (possibly duplicate)");
            }
        }


        public static void run(ContactBST tree)
        {
            if (tree == null)
            {
                Console.WriteLine("Error: the tree is not initialized");
                return;
            }

            int choice;
            do
            {
                PrintMenu();
                if (!int.TryParse(Console.ReadLine(), out choice))
                {
                    Console.WriteLine("Invalid input!");
                    continue;
                }

                switch (choice)
                {
                    case 1:
                        ShowAllContacts(tree);
                        break;
                    case 2:
                        SearchContact(tree);
                        break;
                    case 3:
                        AddContact(tree);
                        break;
                    case 4:
                        Console.WriteLine("Exit...");
                        break;
                    default:
                        Console.WriteLine("Invalid choice!");
                        break;
                }
            } while (choice != 4);
        }
    }
}
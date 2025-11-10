using System;
using System.Collections.Generic;
using System.IO;


namespace ConsoleApplication2
{
    public class FileStorage
    {
        private const string FILE_NAME = "contacts.txt";
        private const char SEPARATOR = '|';

        public static void Save(ContactBST tree)
        {
            try
            {
                List<string> lines = new List<string>();

                tree.Inorder(contact =>
                {
                    string line = $"{contact.Name}{SEPARATOR}" +
                                  $"{contact.Surname}{SEPARATOR}" +
                                  $"{contact.Phone}{SEPARATOR}" +
                                  $"{contact.Email}";
                    lines.Add(line);
                });

                File.WriteAllLines(FILE_NAME, lines.ToArray());
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }

        public static ContactBST Load()
        {
            ContactBST tree = new ContactBST();
            if (!File.Exists(FILE_NAME))
            {
                return tree;
            }

            try
            {
                string[] lines = File.ReadAllLines(FILE_NAME);
                foreach (string line in lines)
                {
                    if (string.IsNullOrEmpty(line)) continue;

                    string[] parts = line.Split(SEPARATOR);
                    if (parts.Length == 4)
                    {
                        Contact contact = Contact.Create(parts[0], parts[1], parts[2], parts[3]);
                        tree.Insert(contact);
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }

            return tree;
        }
    }
}
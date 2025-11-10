using System;


namespace ConsoleApplication2
{
    public class ContactBST
    {
        public BSTNode Root { get; set; }
        public int Size { get; set; }

        public ContactBST()
        {
            Root = null;
            Size = 0;
        }

        public void Clear()
        {
            Root = null;
            Size = 0;
        }

        public bool Insert(Contact contact)
        {
            if (contact == null) return false;

            BSTNode newNode = new BSTNode(contact);

            if (Root == null)
            {
                Root = newNode;
                Size = 1;
                return true;
            }

            BSTNode current = Root;

            while (true)
            {
                int cmp = CompareContacts(contact, current.contact);

                if (cmp < 0)
                {
                    if (current.Left == null)
                    {
                        current.Left = newNode;
                        break;
                    }

                    current = current.Left;
                }
                else if (cmp > 0)
                {
                    if (current.Right == null)
                    {
                        current.Right = newNode;
                        break;
                    }

                    current = current.Right;
                }
                else
                {
                    return false;
                }
            }

            Size++;
            return true;
        }

        public void Inorder(Action<Contact> action)
        {
            if (action == null) return;
            InorderHelp(Root, action);
        }

        public void InorderHelp(BSTNode node, Action<Contact> action)
        {
            if (node == null) return;
            InorderHelp(node.Left, action);
            action(node.contact);
            InorderHelp(node.Right, action);
        }


        public BSTNode SearchByName(string name)
        {
            if (string.IsNullOrEmpty(name)) return null;
            return SearchByNameHelp(Root, name);
        }

        public BSTNode SearchByNameHelp(BSTNode node, string name)
        {
            if (node == null) return null;

            int cmp = string.Compare(name, node.contact.Name);

            if (cmp == 0) return node;
            if (cmp < 0) return SearchByNameHelp(node.Left, name);
            return SearchByNameHelp(node.Right, name);
        }

        public void SearchAllFields(string searchTerm, Action<Contact> action)
        {
            if (string.IsNullOrEmpty(searchTerm) || action == null) return;
            SearchAllFieldsHelp(Root, searchTerm, action);
        }

        public void SearchAllFieldsHelp(BSTNode node, string searchTerm, Action<Contact> action)
        {
            if (node == null) return;

            if (node.contact.Contains(searchTerm))
            {
                action(node.contact);
            }

            SearchAllFieldsHelp(node.Left, searchTerm, action);
            SearchAllFieldsHelp(node.Right, searchTerm, action);
        }

        public void SearchByNameAndSurname(string name, string surname, Action<Contact> action)
        {
            if (string.IsNullOrEmpty(name) || string.IsNullOrEmpty(surname) || action == null) return;
            SearchByNameAndSurnameHelp(Root, name, surname, action);
        }

        public void SearchByNameAndSurnameHelp(BSTNode node, string name, string surname, Action<Contact> action)
        {
            if (node == null) return;

            if (string.Equals(node.contact.Name, name) && string.Equals(node.contact.Surname, surname))
            {
                action(node.contact);
            }

            SearchByNameAndSurnameHelp(node.Left, name, surname, action);
            SearchByNameAndSurnameHelp(node.Right, name, surname, action);
        }


        public static int CompareContacts(Contact a, Contact b)
        {
            if (a == null && b == null) return 0;
            if (a == null) return -1;
            if (b == null) return 1;

            int nameCmp = string.Compare(a.Name, b.Name);
            if (nameCmp != 0) return nameCmp;

            return string.Compare(a.Surname, b.Surname);
        }
    }
}
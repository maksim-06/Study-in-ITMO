namespace ConsoleApplication2
{
    public class BSTNode
    {
        public Contact contact { get; set; }
        public BSTNode Left { get; set; }
        public BSTNode Right { get; set; }

        public BSTNode(Contact contact)
        {
            this.contact = contact;
            Left = null;
            Right = null;
        }
    }
}
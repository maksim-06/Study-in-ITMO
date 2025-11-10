namespace ConsoleApplication2
{
    internal class Program
    {
        public static void Main(string[] args)
        {
            ContactBST contactTree = FileStorage.Load();

            UI.run(contactTree);

            FileStorage.Save(contactTree);
        }
    }
}
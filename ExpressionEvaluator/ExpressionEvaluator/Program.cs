using System.Linq.Expressions;
using System.Text;
using ExpressionEvaluator;

class Program
{
    private static Dictionary<string, long> _variables = new Dictionary<string, long>();
    private static Node _ast;
    private static Func<Dictionary<string, long>, long> _compiledFunc;
    private static StringBuilder _outputBuilder = new();

    static void Main(string[] args)
    {
        Console.WriteLine("Expression Evaluator (Type 'exit' to quit)");


        while (true)
        {
            Console.Write("> ");
            string line = Console.ReadLine()?.Trim();

            if (string.IsNullOrEmpty(line)) continue;

            string[] parts = line.Split(' ', StringSplitOptions.RemoveEmptyEntries);

            if (parts.Length == 0) continue;

            switch (parts[0].ToLower())
            {
                case "expr":
                    if (parts.Length != 2)
                    {
                        Console.WriteLine("Usage: expr <expression>");
                        break;
                    }

                    try
                    {
                        string expr = string.Join(" ", parts, 1, parts.Length - 1);
                        ParseExpression(expr);
                        Console.WriteLine("Expression set");
                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine($"Error: {ex.Message}");
                    }

                    break;

                case "set":
                    if (parts.Length != 3)
                    {
                        Console.WriteLine("Usage: set <variable> <value>");
                        break;
                    }

                    try
                    {
                        string varName = parts[1];
                        long value = long.Parse(parts[2]);
                        _variables[varName] = value;
                        Console.WriteLine($"Set {varName} = {value}");
                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine($"Error: {ex.Message}");
                    }

                    break;
                case "do":
                    try
                    {
                        EvaluateExpression();
                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine($"Error: {ex.Message}");
                    }

                    break;

                case "exit":
                    Console.WriteLine("Goodbye!");
                    return;

                default:
                    Console.WriteLine($"Unknown command: {parts[0]}");
                    Console.WriteLine("Available commands: expr, set, do, exit");
                    break;
            }
        }
    }


    private static void ParseExpression(string exprText)
    {
        var parser = new Parser(exprText);
        _ast = parser.ParseExpression();
        _outputBuilder.Clear();

        var varsParam = Expression.Parameter(typeof(Dictionary<string, long>), "vars");
        Expression body = _ast.ToExpression(varsParam);
        var lambda = Expression.Lambda<Func<Dictionary<string, long>, long>>(body, varsParam);
        _compiledFunc = lambda.Compile();
    }

    private static void EvaluateExpression()
    {
        if (_ast == null || _compiledFunc == null)
        {
            Console.WriteLine("No expression set. Use 'expr <expression>' first.");
            return;
        }

        _outputBuilder.Clear();

        long astResult = _ast.Evaluate(_variables, _outputBuilder);

        long compiledResult = _compiledFunc(_variables);

        if (astResult != compiledResult)
            Console.WriteLine($"Warning: Results differ! AST={astResult}, Compiled={compiledResult}");

        Console.Write(_outputBuilder.ToString());
    }
}
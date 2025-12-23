using System.Linq.Expressions;
using System.Text;

namespace ExpressionEvaluator
{
    public class BinaryNode : Node
    {
        public Node Left { get; set; }
        public Node Right { get; set; }

        public char Operator { get; }

        private static readonly Dictionary<char, int> Priority = new()
        {
            ['+'] = 1, ['-'] = 1,
            ['*'] = 2, ['/'] = 2
        };

        public BinaryNode(Node left, char op, Node right)
        {
            Left = left;
            Operator = op;
            Right = right;
        }

        public override Expression ToExpression(ParameterExpression varsParam, StringBuilder output = null)
        {
            var leftEpr = Left.ToExpression(varsParam, output);
            var rightExpr = Right.ToExpression(varsParam, output);

            return Operator switch
            {
                '+' => Expression.Add(leftEpr, rightExpr),
                '-' => Expression.Subtract(leftEpr, rightExpr),
                '*' => Expression.Multiply(leftEpr, rightExpr),
                '/' => Expression.Divide(leftEpr, rightExpr),
                _ => throw new Exception($"Unknown operator '{Operator}'")
            };
        }

        public override long Evaluate(Dictionary<string, long> vars, StringBuilder output = null)
        {
            long leftVal = Left.Evaluate(vars, output);
            long rightVal = Right.Evaluate(vars, output);
            long result = Operator switch
            {
                '+' => leftVal + rightVal,
                '-' => leftVal - rightVal,
                '*' => leftVal * rightVal,
                '/' => rightVal == 0 ? throw new DivideByZeroException() : leftVal / rightVal,
                _ => throw new Exception($"Unknown operator '{Operator}'")
            };

            if (output != null)
            {
                string exprStr = ToStringWithParentheses(0);
                string displayStr = exprStr.StartsWith('(') && exprStr.EndsWith(')')
                    ? exprStr.Substring(1, exprStr.Length - 2)
                    : exprStr;
                output.AppendLine($"{displayStr} = {result}");
            }

            return result;
        }

        public override string ToStringWithParentheses(int parentPriority)
        {
            int currentPriority = Priority[Operator];
            string leftStr = Left.ToStringWithParentheses(currentPriority);
            string rightStr = Right.ToStringWithParentheses(currentPriority);

            bool leftNeedsParen = Left is BinaryNode leftBin &&
                                  (Priority[leftBin.Operator] < currentPriority ||
                                   (Priority[leftBin.Operator] == currentPriority && Operator != '*'));

            bool rightNeedsParen = Right is BinaryNode rightBin &&
                                   (Priority[rightBin.Operator] < currentPriority ||
                                    (Priority[rightBin.Operator] == currentPriority && Operator == '/'));

            string result = $"{leftStr}{Operator}{rightStr}";
            return parentPriority > currentPriority ? $"({result})" : result;
        }
    }
}
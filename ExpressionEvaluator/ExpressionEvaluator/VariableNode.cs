using System.Linq.Expressions;
using System.Text;

namespace ExpressionEvaluator
{
    public class VariableNode : Node
    {
        public String Name { get; }

        public VariableNode(string name) => Name = name;

        public override Expression ToExpression(ParameterExpression varsParam, StringBuilder output = null)
        {
            return Expression.Convert(
                Expression.Property(
                    varsParam,
                    "Item",
                    Expression.Constant(Name)
                ),
                typeof(long)
            );
        }

        public override long Evaluate(Dictionary<string, long> vars, StringBuilder output = null)
        {
            if (!vars.TryGetValue(Name, out long value))
                throw new Exception($"Variable '{Name}' not set");
            return value;
        }

        public override string ToStringWithParentheses(int parentPriority)
        {
            return Name;
        }
    }
}
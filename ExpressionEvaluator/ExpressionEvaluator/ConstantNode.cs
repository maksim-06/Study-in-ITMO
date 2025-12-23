using System.Linq.Expressions;
using System.Text;

namespace ExpressionEvaluator
{
    public class ConstantNode : Node
    {
        public long Value { get; }

        public ConstantNode(long value)
        {
            Value = value;
        }

        public override Expression ToExpression(ParameterExpression varsParam, StringBuilder output = null)
        {
            return Expression.Constant(Value, typeof(long));
        }

        public override long Evaluate(Dictionary<string, long> vars, StringBuilder output = null)
        {
            return Value;
        }

        public override string ToStringWithParentheses(int parentPriority)
        {
            return Value.ToString();
        }
    }
}
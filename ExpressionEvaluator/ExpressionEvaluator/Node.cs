using System.Linq.Expressions;
using System.Text;

namespace ExpressionEvaluator
{
    public abstract class Node
    {
        public abstract Expression ToExpression(ParameterExpression varsParam, StringBuilder output = null);

        public abstract long Evaluate(Dictionary<string, long> vars, StringBuilder output = null);

        public abstract string ToStringWithParentheses(int parentPriority);
    }
}
namespace ExpressionEvaluator
{
    public class Parser
    {
        private readonly string _input;
        private int _position;
        private char CurrentChar => _position < _input.Length ? _input[_position] : '\0';

        private static readonly Dictionary<char, int> BinaryPriority = new()
        {
            ['+'] = 1, ['-'] = 1,
            ['*'] = 2, ['/'] = 2
        };

        public Parser(string input)
        {
            _input = input.Replace(" ", "");
        }

        private void Advance() => _position++;

        private bool Match(char expected)
        {
            if (CurrentChar == expected)
            {
                Advance();
                return true;
            }

            return false;
        }

        public Node ParseExpression(int precedence = 0)
        {
            Node left = ParsePrefix();
            while (_position < _input.Length && CurrentChar != ')')
            {
                if (!BinaryPriority.ContainsKey(CurrentChar))
                {
                    break;
                }

                int opPrecedence = BinaryPriority[CurrentChar];
                if (opPrecedence <= precedence)
                {
                    break;
                }

                char op = CurrentChar;
                Advance();
                Node right = ParseExpression(opPrecedence);
                left = new BinaryNode(left, op, right);
            }

            return left;
        }


        private Node ParsePrefix()
        {
            if (char.IsDigit(CurrentChar))
                return ParseNumber();
            if (char.IsLetter(CurrentChar))
                return ParseVariable();
            if (Match('('))
            {
                var node = ParseExpression();
                if (!Match(')'))
                    throw new Exception("Expected ')'");
                return node;
            }

            throw new Exception($"Unexpected character: {CurrentChar}");
        }


        private Node ParseNumber()
        {
            int start = _position;
            while (_position < _input.Length && char.IsDigit(CurrentChar))
                Advance();
            string numStr = _input.Substring(start, _position - start);
            return new ConstantNode(long.Parse(numStr));
        }


        private Node ParseVariable()
        {
            int start = _position;
            while (_position < _input.Length && (char.IsLetterOrDigit(CurrentChar) || CurrentChar == '_'))
                Advance();
            return new VariableNode(_input.Substring(start, _position - start));
        }
    }
}
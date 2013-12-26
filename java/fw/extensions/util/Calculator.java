package fw.extensions.util;

public class Calculator
{
	private static Calculator _this = new Calculator();
	private static FuncMap _fm = _this.new FuncMap();

	static
	{
		_fm.loadDefaultFunctions();
	}

	// This is kinda the main thing...
	public static double eval(String expr)
	{
		try
		{
			Expression e = parse(expr);
			if(e == null)
				return Double.NaN;

			return e.eval(null, _fm);
		}
		catch(Exception npe)
		{
			return Double.NaN;
		}
	}

	private static Expression parse(String s)
	{
		if(s == null)
			return null;

		return build(s, 0);
	}

	private static Expression build(String s, int indexErrorOffset)
	{

		// do not remove (required condition for functions with no parameters, e.g. Pi())
		if(s.trim().length() == 0)
			return null;

		Stack s1 = _this.new Stack(); // contains expression nodes
		Stack s2 = _this.new Stack(); // contains open brackets ( and operators ^,*,/,+,-

		boolean term = true; // indicates a term should come next, not an operator
		boolean signed = false; // indicates if the current term has been signed
		boolean negate = false; // indicates if the sign of the current term is negated

		for(int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);

			if(c == ' ' || c == '\t' || c == '\n')
				continue;

			if(term)
			{
				if(c == '(')
				{
					if(negate)
						return null;

					s2.push("(");
				}
				else if(!signed && (c == '+' || c == '-'))
				{
					signed = true;
					if(c == '-')
						negate = true; // by default negate is false
				}
				else if(c >= '0' && c <= '9' || c == '.')
				{

					int j = i + 1;
					while(j < s.length())
					{
						c = s.charAt(j);
						if(c >= '0' && c <= '9' || c == '.')
							j++;

						// code to account for "computerized scientific notation"
						else if(c == 'e' || c == 'E')
						{
							j++;

							if(j < s.length())
							{
								c = s.charAt(j);

								if(c != '+' && c != '-' && (c < '0' || c > '9'))
									return null;

								j++;
							}

							while(j < s.length())
							{
								c = s.charAt(j);
								if(c < '0' || c > '9')
									break;
								j++;
							}
							break;
						}
						else
							break;
					}

					double d = 0;
					String _d = s.substring(i, j);

					try
					{
						d = Double.parseDouble(_d);
					}
					catch(Throwable t)
					{
						return null;
					}

					if(negate)
						d = -d;
					s1.push(_this.new ValNode(d));
					i = j - 1;

					negate = false;
					term = false;
					signed = false;
				}
				else if(c != ';' && c != ')' && c != '^' && c != '*' && c != '/' && c != '+' && c != '-')
				{
					int j = i + 1;
					while(j < s.length())
					{
						c = s.charAt(j);
						if(c != ';' && c != ' ' && c != '\t' && c != '\n' && c != '(' && c != ')' && c != '^' && c != '*' && c != '/' && c != '+' && c != '-')
							j++;
						else
							break;
					}

					if(j < s.length())
					{
						int k = j;
						while(c == ' ' || c == '\t' || c == '\n')
						{
							k++;
							if(k == s.length())
								break;
							c = s.charAt(k);
						}

						if(c == '(')
						{
							FuncNode fn = _this.new FuncNode(s.substring(i, j), negate);
							int b = 1;
							int kOld = k + 1;
							while(b != 0)
							{
								k++;

								if(k >= s.length())
									return null;

								c = s.charAt(k);

								if(c == ')')
									b--;
								else if(c == '(')
									b++;
								else if(c == ';' && b == 1)
								{
									Expression o = build(s.substring(kOld, k), kOld);
									if(o == null)
										return null;
									fn.add(o);
									kOld = k + 1;
								}
							}
							Expression o = build(s.substring(kOld, k), kOld);
							if(o == null)
							{
								if(fn.numChildren() > 0)
									return null;
							}
							else
								fn.add(o);
							s1.push(fn);
							i = k;
						}
						else
						{
							s1.push(_this.new VarNode(s.substring(i, j), negate));
							i = k - 1;
						}
					}
					else
					{
						s1.push(_this.new VarNode(s.substring(i, j), negate));
						i = j - 1;
					}

					negate = false;
					term = false;
					signed = false;
				}
				else
					return null;
			}
			else if(c == ')')
			{
				Stack s3 = _this.new Stack();
				Stack s4 = _this.new Stack();
				while(true)
				{
					if(s2.isEmpty())
						return null;
					Object o = s2.pop();
					if(o.equals("("))
						break;
					s3.addToTail(s1.pop());
					s4.addToTail(o);
				}
				s3.addToTail(s1.pop());

				s1.push(build(s3, s4));
			}
			else if(c == '^' || c == '*' || c == '/' || c == '+' || c == '-')
			{
				term = true;
				s2.push(String.valueOf(c));
			}
			else
				return null;
		}

		if(s1.size() != s2.size() + 1)
			return null;

		return build(s1, s2);
	}

	private static Expression build(Stack s1, Stack s2)
	{
		Stack s3 = _this.new Stack();
		Stack s4 = _this.new Stack();

		while(!s2.isEmpty())
		{
			Object o = s2.removeTail();
			Object o1 = s1.removeTail();
			Object o2 = s1.removeTail();

			if(o.equals("^"))
				s1.addToTail(_this.new PowNode((Expression) o1, (Expression) o2));
			else
			{
				s1.addToTail(o2);
				s4.push(o);
				s3.push(o1);
			}
		}

		s3.push(s1.pop());

		while(!s4.isEmpty())
		{
			Object o = s4.removeTail();
			Object o1 = s3.removeTail();
			Object o2 = s3.removeTail();

			if(o.equals("*"))
				s3.addToTail(_this.new MultNode((Expression) o1, (Expression) o2));
			else if(o.equals("/"))
				s3.addToTail(_this.new DivNode((Expression) o1, (Expression) o2));
			else
			{
				s3.addToTail(o2);
				s2.push(o);
				s1.push(o1);
			}
		}

		s1.push(s3.pop());

		while(!s2.isEmpty())
		{
			Object o = s2.removeTail();
			Object o1 = s1.removeTail();
			Object o2 = s1.removeTail();

			if(o.equals("+"))
				s1.addToTail(_this.new AddNode((Expression) o1, (Expression) o2));
			else if(o.equals("-"))
				s1.addToTail(_this.new SubNode((Expression) o1, (Expression) o2));
			else
				// should never happen
				return null;
		}

		return (Expression) s1.pop();
	}

	private class AddNode extends OpNode
	{

		public AddNode(Expression leftChild, Expression rightChild)
		{
			super(leftChild, rightChild);
		}

		/**
		Adds the evaluation of the left side to the evaluation of the right side and returns the result.
		*/
		@Override
		public double eval(VarMap v, FuncMap f)
		{
			double a = leftChild.eval(v, f);
			double b = rightChild.eval(v, f);
			return a + b;
		}

		@Override
		public String getSymbol()
		{
			return "+";
		}
	}

	private class DivNode extends OpNode
	{

		public DivNode(Expression leftChild, Expression rightChild)
		{
			super(leftChild, rightChild);
		}

		/**
		Divides the evaluation of the left side by the evaluation of the right side and returns the result.
		*/
		@Override
		public double eval(VarMap v, FuncMap f)
		{
			double a = leftChild.eval(v, f);
			double b = rightChild.eval(v, f);
			return a / b;
		}

		@Override
		public String getSymbol()
		{
			return "/";
		}
	}

	private class MultNode extends OpNode
	{

		public MultNode(Expression leftChild, Expression rightChild)
		{
			super(leftChild, rightChild);
		}

		/**
		Multiples the evaluation of the left side and the evaluation of the right side and returns the result.
		*/
		@Override
		public double eval(VarMap v, FuncMap f)
		{
			double a = leftChild.eval(v, f);
			double b = rightChild.eval(v, f);
			return a * b;
		}

		@Override
		public String getSymbol()
		{
			return "*";
		}
	}

	private class SubNode extends OpNode
	{

		public SubNode(Expression leftChild, Expression rightChild)
		{
			super(leftChild, rightChild);
		}

		/**
		Subtracts the evaluation of the right side from the evaluation of the left side and returns the result.
		*/
		@Override
		public double eval(VarMap v, FuncMap f)
		{
			double a = leftChild.eval(v, f);
			double b = rightChild.eval(v, f);
			return a - b;
		}

		@Override
		public String getSymbol()
		{
			return "-";
		}
	}

	private abstract class Expression
	{

		protected Expression parent = null;

		/**
		Returns the result of evaluating the expression tree rooted at this node.
		*/
		public abstract double eval(VarMap v, FuncMap f);

		/**
		Returns true if this node is a descendent of the specified node, false otherwise.  By this
		methods definition, a node is a descendent of itself.
		*/
		public boolean isDescendent(Expression x)
		{
			Expression y = this;

			while(y != null)
			{
				if(y == x)
					return true;
				y = y.parent;
			}

			return false;
		}

		/**
		Protected method used to verify that the specified expression can be included as a child
		expression of this node.

		@throws IllegalArgumentException If the specified expression is not accepted.
		*/
		protected void checkBeforeAccept(Expression x)
		{
			if(x == null)
				throw new IllegalArgumentException("expression cannot be null");

			if(x.parent != null)
				throw new IllegalArgumentException("expression must be removed parent");

			if(isDescendent(x))
				throw new IllegalArgumentException("cyclic reference");
		}

		/**
		Returns a string that represents the expression tree rooted at this node.
		*/
		@Override
		public String toString()
		{
			StringBuffer sb = new StringBuffer();
			toString(this, sb);
			return sb.toString();
		}

		private void toString(Expression x, StringBuffer sb)
		{
			if(x instanceof OpNode)
			{
				OpNode o = (OpNode) x;
				sb.append("(");
				toString(o.leftChild, sb);
				sb.append(o.getSymbol());
				toString(o.rightChild, sb);
				sb.append(")");
			}
			else if(x instanceof TermNode)
			{
				TermNode t = (TermNode) x;

				if(t.getNegate())
				{
					sb.append("(");
					sb.append("-");
				}

				sb.append(t.getName());

				if(t instanceof FuncNode)
				{
					FuncNode f = (FuncNode) t;

					sb.append("(");

					if(f.numChildren() > 0)
						toString(f.child(0), sb);

					for(int i = 1; i < f.numChildren(); i++)
					{
						sb.append(", ");
						toString(f.child(i), sb);
					}

					sb.append(")");
				}

				if(t.getNegate())
					sb.append(")");
			}
			else if(x instanceof ValNode)
				sb.append(((ValNode) x).val);
		}
	}

	private class ValNode extends Expression
	{

		protected double val = 0.0;

		public ValNode(double d)
		{
			val = d;
		}

		/**
		Returns the value.
		*/
		@Override
		public double eval(VarMap v, FuncMap f)
		{
			return val;
		}
	}

	private class FuncNode extends TermNode
	{

		private Bag bag = new Bag(1);
		private double[] of = new double[1];

		public FuncNode(String name, boolean negate)
		{
			super(name, negate);
		}

		/**
		Adds the expression as a child.
		*/
		public void add(Expression x)
		{
			insert(x, bag.size());
		}

		/**
		Adds the expression as a child at the specified index.
		*/
		public void insert(Expression x, int i)
		{
			checkBeforeAccept(x);
			int oldCap = bag.getCapacity();
			bag.insert(x, i);
			int newCap = bag.getCapacity();

			if(oldCap != newCap)
				of = new double[newCap];

			x.parent = this;
		}

		/**
		Returns the number of child expressions.
		*/
		public int numChildren()
		{
			return bag.size();
		}

		/**
		Returns the child expression at the specified index.
		*/
		public Expression child(int i)
		{
			return (Expression) bag.get(i);
		}

		/**
		Evaluates each of the children, storing the result in an internal double array.  The FuncMap is
		used to obtain a Function object based on the name of this FuncNode.  The function is passed
		the double array and returns a result.  If negate is true, the result is negated.  The result
		is then returned.  The numParam passed to the function is the number of children of this FuncNode.
		*/
		@Override
		public double eval(VarMap v, FuncMap f)
		{
			int numParam = bag.size();

			for(int i = 0; i < numParam; i++)
				of[i] = child(i).eval(v, f);

			double result = f.getFunction(name, numParam).of(of, numParam);

			if(negate)
				result = -result;

			return result;
		}
	}

	private class Bag
	{

		protected Object[] data = null;
		protected int size = 0;

		public Bag(int initialCapacity)
		{
			data = new Object[initialCapacity];
		}

		public int size()
		{
			return size;
		}

		public void insert(Object o, int i)
		{
			if(i < 0 || i > size)
				throw new IllegalArgumentException("required: (i >= 0 && i <= size) but: (i = " + i + ", size = " + size + ")");

			ensureCapacity(size + 1);

			for(int j = size; j > i; j--)
				data[j] = data[j - 1];

			data[i] = o;
			size++;
		}

		public void ensureCapacity(int minCapacity)
		{
			if(minCapacity > data.length)
			{
				int x = 2 * data.length;

				if(x < minCapacity)
					x = minCapacity;

				Object[] arr = new Object[x];

				for(int i = 0; i < size; i++)
					arr[i] = data[i];

				data = arr;
			}
		}

		public int getCapacity()
		{
			return data.length;
		}

		public Object get(int i)
		{
			if(i < 0 || i >= size)
				throw new IllegalArgumentException("required: (i >= 0 && i < size) but: (i = " + i + ", size = " + size + ")");

			return data[i];
		}
	}

	private abstract class TermNode extends Expression
	{

		protected String name = null;
		protected boolean negate = false;

		public TermNode(String name, boolean negate)
		{
			setName(name);
			setNegate(negate);
		}

		/**
		Returns true if the term should negate the result before returning it in the eval method.
		*/
		public boolean getNegate()
		{
			return negate;
		}

		public void setNegate(boolean b)
		{
			negate = b;
		}

		/**
		Returns the name of the term.
		*/
		public String getName()
		{
			return name;
		}

		/**
		Sets the name of the term.  Valid names must not begin with a digit or a decimal, and must not contain
		round brackets, operators, commas or whitespace.

		@throws IllegalArgumentException If the name is null or invalid.
		*/
		public void setName(String s)
		{
			if(s == null)
				throw new IllegalArgumentException("name cannot be null");

			if(!isValidName(s))
				throw new IllegalArgumentException("invalid name: " + s);

			name = s;
		}

		private boolean isValidName(String s)
		{
			return TermNode_isValidName(s);
		}
	}

	private static boolean TermNode_isValidName(String s)
	{
		if(s.length() == 0)
			return false;

		char c = s.charAt(0);

		if(c >= '0' && c <= '9' || c == '.' || c == ';' || c == '(' || c == ')' || c == '^' || c == '*' || c == '/' || c == '+' || c == '-' || c == ' ' || c == '\t' || c == '\n')
			return false;

		for(int i = 1; i < s.length(); i++)
		{
			c = s.charAt(i);

			if(c == ';' || c == '(' || c == ')' || c == '^' || c == '*' || c == '/' || c == '+' || c == '-' || c == ' ' || c == '\t' || c == '\n')
				return false;
		}

		return true;
	}

	private class VarNode extends TermNode
	{

		public VarNode(String name, boolean negate)
		{
			super(name, negate);
		}

		/**
		Returns the value associated with the variable name in the VarMap.
		*/
		@Override
		public double eval(VarMap v, FuncMap f)
		{
			double val = v.getValue(name);

			if(negate)
				val = -val;

			return val;
		}
	}

	private class VarMap
	{

		private boolean caseSensitive = true;
		private String[] name = new String[2];
		private double[] value = new double[2];
		private int numVars = 0;

		/**
		Returns the value associated with the specified variable name.

		@throws RuntimeException If a matching variable name cannot be found.
		*/
		public double getValue(String varName)
		{
			for(int i = 0; i < numVars; i++)
				if(caseSensitive && name[i].equals(varName) || !caseSensitive && name[i].equalsIgnoreCase(varName))
					return value[i];

			throw new RuntimeException("variable value has not been set: " + varName);
		}
	}

	private class Stack extends LinkedList
	{
		public Object pop()
		{
			return removeHead();
		}

		public void push(Object o)
		{
			addToHead(o);
		}
	}

	private static class LinkedList
	{

		protected Node head = null;
		protected Node tail = null;
		protected int size = 0;

		public LinkedList()
		{}

		public static class Node
		{
			protected Node next = null;
			protected Node prev = null;
			protected Object userObject = null;

			protected Node(LinkedList list, Object userObject)
			{
				this.userObject = userObject;
			}
		}

		protected Node createNode(Object o)
		{
			return new Node(this, o);
		}

		protected void insertBefore(Node n, Object o)
		{
			Node p = createNode(o);

			if(size == 0)
			{
				head = p;
				tail = p;
			}
			else if(n == head)
			{
				p.next = head;
				head.prev = p;
				head = p;
			}
			else
			{
				n.prev.next = p;
				p.prev = n.prev;
				n.prev = p;
				p.next = n;
			}

			size++;
		}

		protected void insertAfter(Node n, Object o)
		{
			Node p = createNode(o);

			if(size == 0)
			{
				head = p;
				tail = p;
			}
			else if(n == tail)
			{
				p.prev = tail;
				tail.next = p;
				tail = p;
			}
			else
			{
				n.next.prev = p;
				p.next = n.next;
				n.next = p;
				p.prev = n;
			}

			size++;
		}

		protected Object removeNode(Node n)
		{
			if(size == 0)
				return null;

			Object o = n.userObject;

			if(n == head)
			{
				head = head.next;

				if(head == null)
					tail = null;
				else
					head.prev = null;
			}
			else if(n == tail)
			{
				tail = tail.prev;
				tail.next = null;
			}
			else
			{
				n.prev.next = n.next;
				n.next.prev = n.prev;
			}

			size--;
			return o;
		}

		public void addToHead(Object o)
		{
			insertBefore(head, o);
		}

		public void addToTail(Object o)
		{
			insertAfter(tail, o);
		}

		public Object removeHead()
		{
			return removeNode(head);
		}

		public Object removeTail()
		{
			return removeNode(tail);
		}

		public int size()
		{
			return size;
		}

		public boolean isEmpty()
		{
			return size == 0;
		}

		@Override
		public String toString()
		{
			StringBuffer sb = new StringBuffer(6 * size);
			sb.append("[");
			Node n = head;

			if(n != null)
			{
				sb.append(n.userObject);
				n = n.next;
			}

			while(n != null)
			{
				sb.append(", ");
				sb.append(n.userObject);
				n = n.next;
			}

			return sb.append("]").toString();
		}
	}

	private class PowNode extends OpNode
	{

		public PowNode(Expression leftChild, Expression rightChild)
		{
			super(leftChild, rightChild);
		}

		/**
		Raises the evaluation of the left side to the power of the evaluation of the right side and returns the result.
		*/
		@Override
		public double eval(VarMap v, FuncMap f)
		{
			double a = leftChild.eval(v, f);
			double b = rightChild.eval(v, f);
			return Math.pow(a, b);
		}

		@Override
		public String getSymbol()
		{
			return "^";
		}
	}

	private abstract class OpNode extends Expression
	{

		protected Expression leftChild = null;
		protected Expression rightChild = null;

		public OpNode(Expression leftChild, Expression rightChild)
		{
			setLeftChild(leftChild);
			setRightChild(rightChild);
		}

		public void setLeftChild(Expression x)
		{
			checkBeforeAccept(x);
			if(leftChild != null)
				leftChild.parent = null;
			x.parent = this;
			leftChild = x;
		}

		public void setRightChild(Expression x)
		{
			checkBeforeAccept(x);
			if(rightChild != null)
				rightChild.parent = null;
			x.parent = this;
			rightChild = x;
		}

		/**
		Returns the text symbol that represents the operation.
		*/
		public abstract String getSymbol();
	}

	private class FuncMap
	{

		private String[] name = new String[50];
		private Function[] func = new Function[50];
		private int numFunc = 0;
		private boolean caseSensitive = false;

		/**
		Adds the mappings for many common functions.  The names are specified in all lowercase letters.
		*/
		public void loadDefaultFunctions()
		{
			// >= 0 parameters
			setFunction("min", new MinFunction());
			setFunction("max", new MaxFunction());

			// > 0 parameters
			setFunction("sum", new SumFunction());
			setFunction("avg", new AvgFunction());

			// 0 parameters
			setFunction("pi", new PiFunction());
			setFunction("e", new EFunction());
			setFunction("rand", new RandFunction());

			// 1 parameter
			setFunction("sin", new SinFunction());
			setFunction("cos", new CosFunction());
			setFunction("tan", new TanFunction());
			setFunction("sqrt", new SqrtFunction());
			setFunction("abs", new AbsFunction());
			setFunction("ceil", new CeilFunction());
			setFunction("floor", new FloorFunction());
			setFunction("exp", new ExpFunction());
			setFunction("lg", new LgFunction());
			setFunction("ln", new LnFunction());
			setFunction("sign", new SignFunction());
			setFunction("round", new RoundFunction());
			setFunction("fact", new FactFunction());
			setFunction("cosh", new CoshFunction());
			setFunction("sinh", new SinhFunction());
			setFunction("tanh", new TanhFunction());
			setFunction("acos", new AcosFunction());
			setFunction("asin", new AsinFunction());
			setFunction("atan", new AtanFunction());
			setFunction("acosh", new AcoshFunction());
			setFunction("asinh", new AsinhFunction());
			setFunction("atanh", new AtanhFunction());

			// 2 parameters
			setFunction("pow", new PowFunction());
			setFunction("mod", new ModFunction());
			setFunction("combin", new CombinFunction());

			// 1 or 2 parameters
			setFunction("log", new LogFunction());
		}

		/**
		Returns a function based on the name and the specified number of parameters.

		@throws RuntimeException If no supporting function can be found.
		*/
		public Function getFunction(String funcName, int numParam)
		{
			for(int i = 0; i < numFunc; i++)
				if(func[i].acceptNumParam(numParam) && (caseSensitive && name[i].equals(funcName) || !caseSensitive && name[i].equalsIgnoreCase(funcName)))
					return func[i];

			throw new RuntimeException("function not found: " + funcName + " " + numParam);
		}

		/**
		Assigns the name to map to the specified function.

		@throws IllegalArgumentException If any of the parameters are null.
		*/
		public void setFunction(String funcName, Function f)
		{
			if(funcName == null)
				throw new IllegalArgumentException("function name cannot be null");

			if(f == null)
				throw new IllegalArgumentException("function cannot be null");

			for(int i = 0; i < numFunc; i++)
				if(caseSensitive && name[i].equals(funcName) || !caseSensitive && name[i].equalsIgnoreCase(funcName))
				{
					func[i] = f;
					return;
				}

			if(numFunc == name.length)
			{
				String[] tmp1 = new String[2 * numFunc];
				Function[] tmp2 = new Function[tmp1.length];

				for(int i = 0; i < numFunc; i++)
				{
					tmp1[i] = name[i];
					tmp2[i] = func[i];
				}

				name = tmp1;
				func = tmp2;
			}

			name[numFunc] = funcName;
			func[numFunc] = f;
			numFunc++;
		}
	}

	private interface Function
	{

		/**
		Takes the specified double array as input and returns a double value.  Functions
		that accept a variable number of inputs can take numParam to be the number of inputs.
		*/
		public double of(double[] param, int numParam);

		/**
		Returns true if the numParam is an accurate representation of the number of inputs
		the function processes.
		*/
		public boolean acceptNumParam(int numParam);

	}

	private class AbsFunction implements Function
	{

		public AbsFunction()
		{}

		/**
		Returns the positive value of the value stored at index location 0.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return Math.abs(d[0]);
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "abs(x)";
		}
	}

	private class AcosFunction implements Function
	{

		public AcosFunction()
		{}

		/**
		Returns the arc cosine of the value at index location 0.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return java.lang.Math.acos(d[0]);
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "acos(x)";
		}
	}

	private class AcoshFunction implements Function
	{

		public AcoshFunction()
		{}

		/**
		Returns the value of 2 * ln(sqrt((x+1)/2) + sqrt((x-1)/2)), where x is the
		value at index location 0.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			double a = Math.sqrt((d[0] + 1) / 2);
			double b = Math.sqrt((d[0] - 1) / 2);
			return 2 * Math.log(a + b);
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "acosh(x)";
		}
	}

	private class AsinFunction implements Function
	{

		public AsinFunction()
		{}

		/**
		Returns the arc sine of the value at index location 0.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return java.lang.Math.asin(d[0]);
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "asin(x)";
		}
	}

	private class AsinhFunction implements Function
	{

		public AsinhFunction()
		{}

		/**
		Returns the value of ln(x + sqrt(1 + x<sup>2</sup>)), where x is the value at index
		location 0.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return Math.log(d[0] + Math.sqrt(1 + d[0] * d[0]));
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "asinh(x)";
		}
	}

	private class AtanFunction implements Function
	{

		public AtanFunction()
		{}

		/**
		Returns the arc tangent of the value at index location 0.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return java.lang.Math.atan(d[0]);
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "atan(x)";
		}
	}

	private class AtanhFunction implements Function
	{

		public AtanhFunction()
		{}

		/**
		Returns the value of (ln(1+x) - ln(1-x)) / 2, where x is the value at index location 0.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return (Math.log(1 + d[0]) - Math.log(1 - d[0])) / 2;
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "atanh(x)";
		}
	}

	private class AvgFunction implements Function
	{

		public AvgFunction()
		{}

		/**
		Returns the average of the values in the array from [0, numParam).
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			double sum = 0;

			for(int i = 0; i < numParam; i++)
				sum += d[i];

			return sum / numParam;
		}

		/**
		Returns true for 1 or more parameters, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam > 0;
		}

		@Override
		public String toString()
		{
			return "avg(x1, x2, ..., xn)";
		}
	}

	private class CeilFunction implements Function
	{

		public CeilFunction()
		{}

		/**
		Returns the ceiling of the value at index location 0.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return java.lang.Math.ceil(d[0]);
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "ceil(x)";
		}
	}

	private class CombinFunction implements Function
	{

		public CombinFunction()
		{}

		/**
		Returns the number of ways r items can be chosen from n items.  The value of
		n is (int) d[0] and the value of r is (int) d[1].
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			int n = (int) d[0];
			int r = (int) d[1];
			return PascalsTriangle.nCr(n, r);
		}

		/**
		Returns true only for 2 parameters, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 2;
		}

		@Override
		public String toString()
		{
			return "combin(n, r)";
		}
	}

	private class CosFunction implements Function
	{

		public CosFunction()
		{}

		/**
		Returns the cosine of the angle value at index location 0.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return java.lang.Math.cos(d[0]);
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "cos(x)";
		}
	}

	private class CoshFunction implements Function
	{

		public CoshFunction()
		{}

		/**
		Returns the value of (<i>e<sup>x</sup>&nbsp;+&nbsp;e<sup>-x</sup></i>)/2, where x is the value
		at index location 0 and <i>e</i> is the base of natural logarithms.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return (Math.pow(Math.E, d[0]) + Math.pow(Math.E, -d[0])) / 2;
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "cosh(x)";
		}
	}

	private class EFunction implements Function
	{

		public EFunction()
		{}

		/**
		Returns the constant <i>e</i> regardless of the input.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return java.lang.Math.E;
		}

		/**
		Returns true only for 0 parameters, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 0;
		}

		@Override
		public String toString()
		{
			return "e()";
		}
	}

	private class ExpFunction implements Function
	{

		public ExpFunction()
		{}

		/**
		Returns Euler's number, <i>e</i>, raised to the exponent of the value at index location 0.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return Math.exp(d[0]);
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "exp(x)";
		}
	}

	private class FactFunction implements Function
	{

		public FactFunction()
		{}

		/**
		Takes the (int) of the value at index location 0 and computes the factorial
		of that number.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			int n = (int) d[0];

			double result = 1;

			for(int i = n; i > 1; i--)
				result *= i;

			return result;
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "fact(n)";
		}
	}

	private static final class PascalsTriangle
	{

		private PascalsTriangle()
		{}

		private static double[][] pt = new double[][] { { 1 } };

		/**
		The nCr function returns the number of ways r things can be chosen from a set of size n.
		Mathematically, it is defined as: n! / (r! * (n - r)!)
		Although the result is always a whole number, double precision is used because
		the maximum value a double can represent is larger than long.  Thus, large returned
		values will only be an approximation of the actual value.  If the result exceeds the
		capabilities of double precision then the result can be checked using Double.isInfinite(...).
		For example: System.out.println(PascalsTriangle.nCr(1030, 515)); // outputs: Infinity
		If the value of n or r is less than 0 or the value of r is greater than n then 0 is
		returned.
		*/
		public synchronized static double nCr(int n, int r)
		{
			if(n < 0 || r < 0 || r > n)
				return 0;

			if(n >= pt.length)
			{
				int d = 2 * pt.length;
				double[][] pt2 = null;
				if(n > d)
					pt2 = new double[n + 1][];
				else
					pt2 = new double[d + 1][];

				for(int i = 0; i < pt.length; i++)
					pt2[i] = pt[i];

				for(int i = pt.length; i < pt2.length; i++)
				{
					pt2[i] = new double[i / 2 + 1];

					pt2[i][0] = 1;

					for(int j = 1; j < pt2[i].length; j++)
					{
						double x = pt2[i - 1][j - 1];
						if(j < pt2[i - 1].length)
							x = x + pt2[i - 1][j];
						else
							x = 2 * x;

						pt2[i][j] = x;
					}
				}
				pt = pt2;
			}

			if(2 * r > n)
				r = n - r;

			return pt[n][r];
		}
	}

	private class FloorFunction implements Function
	{
		/**
		Returns the floor of the value at index location 0.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return java.lang.Math.floor(d[0]);
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "floor(x)";
		}
	}

	private class LgFunction implements Function
	{

		public LgFunction()
		{}

		/**
		Returns the log base 2 of the value at index location 0.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return java.lang.Math.log(d[0]) / java.lang.Math.log(2);
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "lg(x)";
		}
	}

	private class LnFunction implements Function
	{

		public LnFunction()
		{}

		/**
		Returns the natural logarithm of the value at index location 0.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return java.lang.Math.log(d[0]);
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "ln(x)";
		}
	}

	private class LogFunction implements Function
	{

		public LogFunction()
		{}

		/**
		If the number of parameters specified is 1, then the log base 10 is taken of the
		value at index location 0.  If the number of parameters specified is 2, then the
		base value is at index location 1.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			if(numParam == 1)
				return java.lang.Math.log(d[0]) / java.lang.Math.log(10);
			return java.lang.Math.log(d[0]) / java.lang.Math.log(d[1]);
		}

		/**
		Returns true only for 1 or 2 parameters, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1 || numParam == 2;
		}

		@Override
		public String toString()
		{
			return "log(x):log(x, y)";
		}
	}

	private class MaxFunction implements Function
	{

		public MaxFunction()
		{}

		/**
		Returns the maximum value of the specified inputs.  Double.MAX_VALUE is returned for 0 parameters.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			if(numParam == 0)
				return Double.MAX_VALUE;

			double max = -Double.MAX_VALUE;
			for(int i = 0; i < numParam; i++)
				if(d[i] > max)
					max = d[i];
			return max;
		}

		/**
		Returns true for 0 or more parameters, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam >= 0;
		}

		@Override
		public String toString()
		{
			return "max(x1, x2, ..., xn)";
		}
	}

	private class MinFunction implements Function
	{

		public MinFunction()
		{}

		/**
		Returns the minimum value of the specified inputs.  Double.MIN_VALUE is returned for 0 parameters.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			if(numParam == 0)
				return Double.MIN_VALUE;

			double min = Double.MAX_VALUE;

			for(int i = 0; i < numParam; i++)
				if(d[i] < min)
					min = d[i];
			return min;
		}

		/**
		Returns true for 0 or more parameters, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam >= 0;
		}

		@Override
		public String toString()
		{
			return "min(x1, x2, ..., xn)";
		}
	}

	private class ModFunction implements Function
	{

		public ModFunction()
		{}

		/**
		Returns the value of x % y, where x = d[0] and y = d[1].  More precisely, the value returned is
		x minus the value of x / y, where x / y is rounded to the closest integer value towards 0.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return d[0] % d[1];
		}

		/**
		Returns true only for 2 parameters, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 2;
		}

		@Override
		public String toString()
		{
			return "mod(x, y)";
		}
	}

	private class PiFunction implements Function
	{

		public PiFunction()
		{}

		/**
		Returns the constant Pi regardless of the input.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return java.lang.Math.PI;
		}

		/**
		Returns true only for 0 parameters, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 0;
		}

		@Override
		public String toString()
		{
			return "pi()";
		}
	}

	private class PowFunction implements Function
	{

		public PowFunction()
		{}

		/**
		Returns the value at index location 0 to the exponent of the value
		at index location 1.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return java.lang.Math.pow(d[0], d[1]);
		}

		/**
		Returns true only for 2 parameters, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 2;
		}

		@Override
		public String toString()
		{
			return "pow(x, y)";
		}
	}

	private class RandFunction implements Function
	{

		public RandFunction()
		{}

		/**
		Returns a random value in the range [0, 1) that does not depend on the input.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return Rnd.nextDouble();
		}

		/**
		Returns true only for 0 parameters, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 0;
		}

		@Override
		public String toString()
		{
			return "rand()";
		}
	}

	private class RoundFunction implements Function
	{

		public RoundFunction()
		{}

		/**
		Returns the value at d[0] rounded to the nearest integer value.
		If the value exceeds the capabilities of long precision then
		the value itself is returned.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			if(SafeMath.isOverflowLong(d[0]))
				return d[0];

			return Math.round(d[0]);
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "round(x)";
		}
	}

	private class SignFunction implements Function
	{

		public SignFunction()
		{}

		/**
		The sign function returns 1 if the d[0] > 0, -1 if d[0] < 0, else 0.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			if(d[0] > 0)
				return 1;
			if(d[0] < 0)
				return -1;
			return 0;
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "sign(x)";
		}
	}

	private class SinFunction implements Function
	{

		public SinFunction()
		{}

		/**
		Returns the sine of the angle value at index location 0.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return java.lang.Math.sin(d[0]);
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "sin(x)";
		}
	}

	private class SinhFunction implements Function
	{

		public SinhFunction()
		{}

		/**
		Returns the value of (<i>e<sup>x</sup>&nbsp;-&nbsp;e<sup>-x</sup></i>)/2,
		where x is the value at index location 0 and <i>e</i> is the base of natural logarithms.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return (Math.pow(Math.E, d[0]) - Math.pow(Math.E, -d[0])) / 2;
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "sinh(x)";
		}
	}

	private class SqrtFunction implements Function
	{

		public SqrtFunction()
		{}

		/**
		Returns the square root of the value at index location 0.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return java.lang.Math.sqrt(d[0]);
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "sqrt(x)";
		}
	}

	private class SumFunction implements Function
	{

		public SumFunction()
		{}

		/**
		Returns the sum of the values in the array from [0, numParam).
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			double sum = 0;

			for(int i = 0; i < numParam; i++)
				sum += d[i];

			return sum;
		}

		/**
		Returns true for 1 or more parameters, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam > 0;
		}

		@Override
		public String toString()
		{
			return "sum(x1, x2, ..., xn)";
		}
	}

	private class TanFunction implements Function
	{

		public TanFunction()
		{}

		/**
		Returns the tangent of the angle value at index location 0.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			return java.lang.Math.tan(d[0]);
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "tan(x)";
		}
	}

	private class TanhFunction implements Function
	{

		public TanhFunction()
		{}

		/**
		Returns the value of (<i>e<sup>x</sup>&nbsp;-&nbsp;e<sup>-x</sup></i>)/(<i>e<sup>x</sup>&nbsp;+&nbsp;e<sup>-x</sup></i>),
		where x is the value at index location 0 and <i>e</i> is the base of natural logarithms.
		*/
		@Override
		public double of(double[] d, int numParam)
		{
			double e = Math.pow(Math.E, 2 * d[0]);
			return (e - 1) / (e + 1);
		}

		/**
		Returns true only for 1 parameter, false otherwise.
		*/
		@Override
		public boolean acceptNumParam(int numParam)
		{
			return numParam == 1;
		}

		@Override
		public String toString()
		{
			return "tanh(x)";
		}
	}
}
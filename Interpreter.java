import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Stack;

/*
 * BF commands:
 * > < : move pointer 
 * + - : modify value at pointer 
 * [ ] : conditional loop 
 * , . : read input / write output
 */

public class Interpreter
{
  private ArrayList<Integer> memory;

  private int pointer;

  private Stack<Integer> callStack;

  private ListIterator<Integer> input;

  /**
   * Interpret a string of BF code with no input. Print memory at end. Memory
   * expandable to right, fixed to left.
   * 
   * @param rawCode
   */
  public void interpret(String rawCode)
  {
    ArrayList<Integer> inputList = new ArrayList<Integer>();
    input = inputList.listIterator();
    execute(rawCode);
  }

  /**
   * Interpret a string of BF code, with a string for input. Print memory at
   * end. Memory expandable to right, fixed to left.
   * 
   * @param rawCode
   * @param rawInput
   */
  public void interpret(String rawCode, String rawInput)
  {
    ArrayList<Integer> inputList = new ArrayList<Integer>();
    for (char c : rawInput.toCharArray())
      {
        inputList.add((int) c);
      }
    input = inputList.listIterator();
    execute(rawCode);
  }

  private void execute(String rawCode)
  {
    memory = new ArrayList<Integer>(100);
    memory.add(0);
    pointer = 0;
    callStack = new Stack<Integer>();
    char[] code = rawCode.toCharArray();
    for (int index = 0; index < code.length; index++)
      {
        char c = code[index];
        switch (c)
          {
          case '>':
            pointer++;
            // expand memory if necessary
            if (pointer > memory.size() - 1)
              {
                memory.add(0);
              }
            ;
            break;
          case '<':
            pointer--;
            // fixed memory to left
            if (pointer < 0)
              {
                pointer = 0;
              }
            ;
            break;
          case '+':
            memory.set(pointer, memory.get(pointer) + 1);
            break;
          case '-':
            memory.set(pointer, memory.get(pointer) - 1);
            break;
          case '[':
            callStack.push(index);
            break;
          case ']':
            // conditional jump
            if (memory.get(pointer) != 0)
              {
                index = callStack.pop() - 1;
              }
            else
              {
                callStack.pop();
              }
            break;
          case ',':
            // read from input
            if (input.hasNext())
              {
                memory.set(pointer, input.next());
              }
            else
              {
                memory.set(pointer, 0);
              }
            break;
          case '.':
            // write output to console
            System.out.print(Character.toChars(memory.get(pointer)));
            break;
          default:
            break;
          }
      }
    System.out.println();
    System.out.println();
    printMemory();
  }

  private void printMemory()
  {
    System.out.println("Memory:");
    int i = 0;
    for (Integer value : memory)
      {
        System.out.print(i + " " + value);
        if (i == pointer)
          {
            System.out.print(" <-- Ptr");
          }
        System.out.println();
        i++;
      }
  }

  public static void main(String[] args)
  {
    Interpreter i = new Interpreter();
    if (args.length == 1)
      {
        i.interpret(args[0]);
      }
    else if (args.length == 2)
      {
        i.interpret(args[0], args[1]);
      }
    else
      {
        //Hello, world!
        String code = "++++++++++[>+++++++>++++++++++>+++>++++<";
        code += "<<<-]>++.>+.+++++++..+++.>>++++.<++.<+++";
        code += "+++++.--------.+++.------.--------.>+.";
        i.interpret(code);
        System.out.println();
        //Reversed
        code = ">,[>,]<[.<]";
        i.interpret(code, "Hello, world!");
      }
  }
}

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class calculator extends JFrame implements ActionListener {

    JTextField display;
    String expression = "";

    public calculator() {

        setTitle("Calculator");
        setSize(360,480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        display = new JTextField();
        display.setFont(new Font("Arial",Font.BOLD,30));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.black);
        display.setForeground(Color.white);
        display.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));
        add(display,BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5,4,10,10));
        panel.setBackground(Color.black);
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        String buttons[]={
                "C","⌫","%","÷",
                "7","8","9","×",
                "4","5","6","-",
                "1","2","3","+",
                "0",".","=",""
        };

        for(String text:buttons){

            if(text.equals("")){
                panel.add(new JLabel());
                continue;
            }

            JButton btn=new JButton(text);
            btn.setFont(new Font("Arial",Font.BOLD,20));
            btn.setFocusPainted(false);
            btn.setForeground(Color.white);

            if(text.equals("C"))
                btn.setBackground(Color.red);

            else if(text.equals("⌫"))
                btn.setBackground(new Color(70,130,180));

            else if(text.equals("+")||text.equals("-")||text.equals("×")||text.equals("÷")||text.equals("="))
                btn.setBackground(new Color(255,140,0));

            else
                btn.setBackground(new Color(60,60,60));

            btn.addActionListener(this);
            panel.add(btn);
        }

        add(panel,BorderLayout.CENTER);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e){

        String cmd = e.getActionCommand();

        if(cmd.equals("C")){
            expression="";
            display.setText("");
        }

        else if(cmd.equals("⌫")){
            if(expression.length()>0){
                expression=expression.substring(0,expression.length()-1);
                display.setText(expression);
            }
        }

        else if(cmd.equals("=")){
            try{

                String exp = expression.replace("×","*").replace("÷","/");
                double result = eval(exp);

                display.setText(""+result);
                expression=""+result;

            }catch(Exception ex){
                display.setText("Error");
                expression="";
            }
        }

        else{
            expression += cmd;
            display.setText(expression);
        }
    }

    public double eval(String exp){
        return new Object(){
            int pos=-1,ch;

            void nextChar(){
                ch=(++pos<exp.length())?exp.charAt(pos):-1;
            }

            boolean eat(int charToEat){
                while(ch==' ')nextChar();
                if(ch==charToEat){
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse(){
                nextChar();
                double x=parseExpression();
                return x;
            }

            double parseExpression(){
                double x=parseTerm();
                for(;;){
                    if(eat('+'))x+=parseTerm();
                    else if(eat('-'))x-=parseTerm();
                    else return x;
                }
            }

            double parseTerm(){
                double x=parseFactor();
                for(;;){
                    if(eat('*'))x*=parseFactor();
                    else if(eat('/'))x/=parseFactor();
                    else return x;
                }
            }

            double parseFactor(){

                if(eat('+'))return parseFactor();
                if(eat('-'))return -parseFactor();

                double x;
                int startPos=this.pos;

                if((ch>='0'&&ch<='9')||ch=='.'){
                    while((ch>='0'&&ch<='9')||ch=='.')nextChar();
                    x=Double.parseDouble(exp.substring(startPos,this.pos));
                }
                else{
                    throw new RuntimeException("Error");
                }

                return x;
            }

        }.parse();
    }

    public static void main(String[] args) {
        new calculator();
    }
}

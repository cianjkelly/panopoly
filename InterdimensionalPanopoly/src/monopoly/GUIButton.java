package monopoly;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class GUIButton {
    private JButton button;

    public GUIButton(String name, int x, int y,MouseAdapter mouseAdapter,GUI gui)
    {
        String htmlname="<html><center>"+name+"</center></html>";
        button=new JButton(htmlname);
        button.setFocusPainted(false);
        button.setVisible(false);
        button.setBounds(x,y,gui.getOFFSET(),30);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createBevelBorder(1,Color.red,Color.red.darker()));
        button.setBackground(Color.black);
        button.setForeground(Color.lightGray);
        int size = 20;
        button.setFont(new Font("Courier", Font.BOLD,  size));
        while(button.getFontMetrics(new Font("Courier", Font.BOLD,  size)).stringWidth(name) > button.getWidth())
        {
            size--;
            button.setFont(new Font("Courier", Font.BOLD,  size));
        }
        if(mouseAdapter!=null)
            button.addMouseListener(mouseAdapter);
        gui.getMainPane().add(button);
    }
    public void setVisible(boolean visibility)
    {
        button.setVisible(visibility);
    }
    public void setEnabled(boolean enable)
    {
        button.setEnabled(enable);
    }
    public void setSize(int width,int height)
    {
        button.setSize(width,height);

    }

    public void setMouseEvent(MouseAdapter mouse)
    {
        button.removeMouseListener(button.getMouseListeners()[0]);
        button.addMouseListener(mouse);
    }
    public boolean isVisible()
    {
        return button.isVisible();
    }
    public void setText(String text) {
        button.setText(text);
        if(!text.contains("00:"))
        {int size = 20;
            button.setFont(new Font("Courier", Font.BOLD,  size));
            while(button.getFontMetrics(new Font("Courier", Font.BOLD,  size)).stringWidth(text) > button.getWidth())
            {
                size--;
                button.setFont(new Font("Courier", Font.BOLD,  size));
            }}
    }

    public JButton getButton() {
        return button;
    }
}

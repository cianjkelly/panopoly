package monopoly;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PlayerLabel
{
    private final int scale;
    private Player player;
    private int index;
    private JLabel name=new JLabel("",SwingConstants.CENTER);
    private JLabel balance=new JLabel("",SwingConstants.CENTER);
    private JLabel icon;
    private GUI gui;
    private JLabel positionIcon;
    public PlayerLabel(Player player, int i, ImageIcon icon,GUI gui)
    {
        this.gui=gui;
        this.player=player;
        this.index=i;
        this.icon=new JLabel(icon);

        this.icon.setBounds(this.gui.getFRAME_SIZE().height,i*110+10,100,100);
        this.icon.setVisible(true);
        this.icon.setBackground(Color.DARK_GRAY);
        this.icon.setOpaque(true);
        gui.getMainPane().add(this.icon);
        gui.getMainPane().add(name);

        name.setText(player.getIdentifier());
        name.setFont(new Font("Arial",Font.BOLD,32));
        name.setBounds(this.gui.getFRAME_SIZE().height+100,i*110+10,200,100);
        name.setVisible(true);
        name.setBackground(Color.DARK_GRAY);
        name.setForeground(Color.white);
        name.setOpaque(true);

        balance.setText("$"+player.getBalance());
        balance.setFont(new Font("Times New Roman",Font.ITALIC,32));
        balance.setBounds(this.gui.getFRAME_SIZE().height+300,i*110+10,100,100);
        balance.setVisible(true);
        balance.setOpaque(true);
        balance.setBackground(Color.DARK_GRAY);
        balance.setForeground(Color.WHITE);
        gui.getMainPane().add(balance);

        BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        icon.paintIcon(null, g, 0,0);
        g.dispose();
        scale=(int)(gui.getLabelWidth()*.5);
        ImageIcon newIcon =new ImageIcon(bi.getScaledInstance(scale,scale,1));
        positionIcon= new JLabel(newIcon);
        positionIcon.setBounds(1,1,scale,scale);
        gui.getMainPane().add(positionIcon,index);
    }
    public void updateLabel()
    {
        balance.setText("$"+player.getBalance());
        positionIcon.setBounds(gui.getLocationLabel(player.getPosition()).getX()+(int)(index%3*(gui.getLabelHeight()/4)),
                gui.getLocationLabel(player.getPosition()).getY()+1+(int)(index%2*(gui.getLabelHeight()/2)),scale,scale);
        positionIcon.updateUI();
    }
}
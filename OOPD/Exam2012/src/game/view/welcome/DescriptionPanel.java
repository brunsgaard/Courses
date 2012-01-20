package game.view.welcome;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.text.View;

public class DescriptionPanel extends JPanel
{

    private static final long serialVersionUID = 5525100863246200340L;
    public JLabel description;
    public JScrollPane jsp;

    public DescriptionPanel(String text)
    {
        JLabel label = new JLabel("<html>" + text
                + "</html>", JLabel.CENTER);

        View view = (View) label
                .getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey);
        view.setSize(400, 0);
        // int height = (int) Math.ceil(view.getPreferredSpan(View.Y_AXIS));

        this.jsp = new JScrollPane(label);
        jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jsp.setPreferredSize(new Dimension(420, 200));
        jsp.setOpaque(false);
        jsp.getViewport().setOpaque(false);
        Border border = BorderFactory.createEmptyBorder( 0, 0, 0, 0 );
        jsp.setBorder( border );
        jsp.setViewportBorder(border);
        add(this.jsp);
    }

}

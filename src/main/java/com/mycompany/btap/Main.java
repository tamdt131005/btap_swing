
package com.mycompany.btap;
import com.mycompany.btap.controller.cAuth;
import com.mycompany.btap.view.vAuth;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                vAuth view = new vAuth();
                new cAuth(view);
            }
        });
    }
}

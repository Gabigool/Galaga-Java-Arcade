package main.java.com.galaga.presenter;

import main.java.com.galaga.view.TitleScreenView;
import java.awt.event.KeyEvent;

public class TitleScreenPresenter {
    private TitleScreenView view;
    private boolean startGame = false;
    
    public void setView(TitleScreenView view) {
        this.view = view;
    }
    
    public void onKeyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_ENTER) {
            startGame = true;
        }
    }
    
    public boolean isStartGame() {
        return startGame;
    }
    
    public void resetStartGame() {
        startGame = false;
    }
}

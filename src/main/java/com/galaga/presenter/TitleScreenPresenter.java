package main.java.com.galaga.presenter;

import main.java.com.galaga.model.HighScore;
import main.java.com.galaga.persistence.repository.HighScoreRepository;
import main.java.com.galaga.view.TitleScreenView;
import java.awt.event.KeyEvent;
import java.util.List;

public class TitleScreenPresenter {
    private TitleScreenView view;
    private HighScoreRepository highScoreRepository;
    
    // Estados del menú
    private int selectedOption = 0;
    private final String[] menuOptions = {
        "🎮 JUGAR",
        "🏆 HIGH SCORES", 
        "❓ CÓMO JUGAR",
        "❌ SALIR"
    };
    
    private boolean startGame = false;
    private boolean showHighScores = false;
    private boolean showInstructions = false;
    private boolean exitGame = false;
    
    public TitleScreenPresenter() {
        this.highScoreRepository = new HighScoreRepository();
    }
    
    public void setView(TitleScreenView view) {
        this.view = view;
    }
    
    public void onKeyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                navigateUp();
                break;
            case KeyEvent.VK_DOWN:
                navigateDown();
                break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                selectCurrentOption();
                break;
            case KeyEvent.VK_ESCAPE:
                if (showHighScores || showInstructions) {
                    // Volver al menú principal
                    showHighScores = false;
                    showInstructions = false;
                    view.showMainMenu();
                }
                break;
        }
    }
    
    private void navigateUp() {
        if (!showHighScores && !showInstructions) {
            selectedOption = (selectedOption - 1 + menuOptions.length) % menuOptions.length;
            view.updateSelectedOption(selectedOption);
        }
    }
    
    private void navigateDown() {
        if (!showHighScores && !showInstructions) {
            selectedOption = (selectedOption + 1) % menuOptions.length;
            view.updateSelectedOption(selectedOption);
        }
    }
    
    private void selectCurrentOption() {
        switch (selectedOption) {
            case 0: // JUGAR
                startGame = true;
                break;
            case 1: // HIGH SCORES
                showHighScores = true;
                view.showHighScoresScreen(getHighScores());
                break;
            case 2: // CÓMO JUGAR
                showInstructions = true;
                view.showInstructionsScreen();
                break;
            case 3: // SALIR
                exitGame = true;
                System.exit(0);
                break;
        }
    }
    
    private List<HighScore> getHighScores() {
        return highScoreRepository.getTopHighScores();
    }
    
    // Getters
    public boolean isStartGame() { return startGame; }
    public boolean isShowHighScores() { return showHighScores; }
    public boolean isShowInstructions() { return showInstructions; }
    public boolean isExitGame() { return exitGame; }
    public String[] getMenuOptions() { return menuOptions; }
    public int getSelectedOption() { return selectedOption; }
    
    public void resetStartGame() {
        startGame = false;
    }
    
    public void resetStates() {
        showHighScores = false;
        showInstructions = false;
        selectedOption = 0;
    }
}

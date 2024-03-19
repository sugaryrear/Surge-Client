package com.client.features.accounts;


import com.client.Buffer;
import com.client.Client;
import com.client.Configuration;
import com.client.Sprite;
import com.client.engine.impl.MouseHandler;
import com.client.sign.Signlink;
import com.client.utilities.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * AccountManager
 * Handles most actions for the account saving and loading.
 *
 * @author Lennard
 */
public class AccountManager {

    /**
     * The maximum amount of Accounts that can be saved.
     */
    private static final int MAXIMUM_ACCOUNTS = 5;

    /**
     * The {@link Path} to the Accounts save file.
     */
    private static final Path ACCOUNTS_FILE = Paths.get(System.getProperty("user.home"), "accounts.dat");
//    private final Path ACCOUNTS_FILE = Paths.get(System.getProperty("user.home"),
//            Configuration.CLIENT_TITLE.toLowerCase() + "account.dat");

    /**
     * A {@link Collection} of holding all saved {@link Account}s.
     */
    private final Collection<Account> accountList = new ArrayList<Account>(MAXIMUM_ACCOUNTS);

    /**
     * The back ground sprite of the account box.
     */
    private Sprite backgroundSprite;

    /**
     * The {@link Client} instance.
     */
    private final Client client;

    public AccountManager(final Client client, Sprite backgroundSprite) {
        this.client = client;
        this.backgroundSprite = backgroundSprite;
    }

    /**
     * Adds an account with the given username and password.
     *
     * @param userName
     *            The username of this account.
     * @param userPassword
     *            The password of this account.
     * @throws IOException
     */
    public void addAccount(String userName, String userPassword) throws IOException {

        if (accountList.size() == 3) {
            client.firstLoginMessage = "Account list full!";
            client.secondLoginMessage = "Delete an account to create space for a new one.";
            return;
        }
        if (userName.length() == 0 || userPassword.length() == 0) {
            client.firstLoginMessage = "Username & Password";
            client.secondLoginMessage = "Must be more than 1 character";
            return;
        }
        Account accountToAdd = new Account(userName, userPassword);
        for (Account account : accountList) {
            if (accountToAdd.getUserName().equals(account.getUserName())) {
                client.firstLoginMessage = "There is already an account";
                client.secondLoginMessage = "saved with that username.";
                return;
            }
        }
        accountList.add(accountToAdd);
        saveAccounts();
    }

    /**
     * Draws the accounts on the login screen.
     */
    public void processAccountDrawing() {
        // Client.adv_font_regular.draw_centered("button at the top right of that page.",100,100,0xffffff, true);
      //  System.out.println("window wid: "+Client.instance.getCanvasWidth() );//765
        int xPosition = Client.instance.getCanvasWidth() / 2;
        final int yPosition = Client.instance.getCanvasHeight() / 2 + 190;
        xPosition -= -3 + (accountList.size() * (106 / 2 + (accountList.size() > 1 ? 10 : 0)));
        for (Account account : accountList) {
            if (accountList.size() > 1) {
                xPosition += 10;
            }
            account.setXPosition(xPosition);
            account.setYPosition(yPosition);
            draw(account);
            xPosition += 106 + 10;
        }
    }
//    int offsetx = 206;
//    int yoffset = 370;
    /**
     * Handles the mouse click actions performed on the account buttons.
     *
     * @throws IOException
     */
    public void processAccountInput() throws IOException {
        ArrayList<Account> accountsToRemove = new ArrayList<Account>();


        for (Account account : accountList) {
            if (MouseHandler.clickMode3 == 1) {
                //  System.out.println(client.cursor_x+" "+account.getXPosition());
                //System.out.println(client.cursor_y+" "+account.getYPosition());
                if (MouseHandler.saveClickX >= (account.getXPosition() + 14)
                        && MouseHandler.saveClickX <= (account.getXPosition() + 106 - 14 )
                        && MouseHandler.saveClickY >= account.getYPosition() + 23
                        && MouseHandler.saveClickY <= account.getYPosition() + 40 ) {
                    accountsToRemove.add(account);
                    client.firstLoginMessage = "Deleted Account:";
                    client.secondLoginMessage = account.getUserName();

                } else if (MouseHandler.saveClickX >= account.getXPosition()
                        && MouseHandler.saveClickX <= account.getXPosition() + 106
                        &&  MouseHandler.saveClickY >= account.getYPosition()
                        &&  MouseHandler.saveClickY <= account.getYPosition() + 23) {
                    client.myUsername = account.getUserName();
                    client.myPassword = account.getUserPassword();
                  //  if(client.loginTimer.finished()) {
                        client.login(client.myUsername, client.myPassword, false);
                        //client.loginTimer.start(2);
                  //  }
                }
            }
        }
        if (!accountsToRemove.isEmpty()) {
            accountList.removeAll(accountsToRemove);
            saveAccounts();
        }
    }

    /**
     * Draws the accounts background sprite, username and delete text.
     *
     * @param account
     */
    private void draw(Account account) {
        backgroundSprite.drawSprite(account.getXPosition(), account.getYPosition());//delete hover
        if (MouseHandler.mouseX  >= account.getXPosition() + 14
                &&MouseHandler.mouseX <= account.getXPosition() + 106 - 14
                && MouseHandler.mouseY >= account.getYPosition() + 23
                && MouseHandler.mouseY  <= account.getYPosition() + 40 ) {
            Sprite backgroundSpriteHover = new Sprite("Login/1849");
            backgroundSpriteHover.drawSprite(account.getXPosition(), account.getYPosition());
        } else if (MouseHandler.mouseX  >= account.getXPosition()
                && MouseHandler.mouseX  <= account.getXPosition() + 106
                && MouseHandler.mouseY >= account.getYPosition()
                && MouseHandler.mouseY <= account.getYPosition() + 23) {
            Sprite backgroundSpriteHover =  new Sprite("Login/1848");
            backgroundSpriteHover.drawSprite(account.getXPosition(), account.getYPosition());
        }
      //  System.out.println("wid: "+backgroundSprite.width);
        Client.instance.newBoldFont.drawCenteredString(account.getUserName(), account.getXPosition() + 110 / 2, account.getYPosition() + 16, 0xFFFFFF, 0);
      //  Client.instance.newBoldFont.drawCenteredString("wordslol", account.getXPosition() + 140 / 2, account.getYPosition() + 16, 0xFFFFFF, 0);

    }

    /**
     * Saves the accounts to a new file, located at the {@code ACCOUNTS_FILE}
     * path.
     *
     * @throws IOException
     */
    public void saveAccounts() throws IOException {
        if (accountList.isEmpty()) {
            Files.deleteIfExists(ACCOUNTS_FILE);
            return;
        }
        Files.deleteIfExists(ACCOUNTS_FILE);
        Files.createFile(ACCOUNTS_FILE);
        Buffer buffer = Buffer.create(30000);
        buffer.writeByte(accountList.size());
        for (Account account : accountList) {
            buffer.writeString(account.getUserName());
            buffer.writeString(account.getUserPassword());
        }
        FileUtils.writeFile(ACCOUNTS_FILE.toString(), Arrays.copyOf(buffer.payload, buffer.currentPosition));
    }

    /**
     * Loads the accounts from the save file.
     *
     * @throws IOException
     */
    public void loadAccounts() throws IOException {
        File file = ACCOUNTS_FILE.toFile();
        if (!file.exists()) {
            return;
        }
        byte[] fileData = FileUtils.read(file);
        Buffer buffer = new Buffer(fileData);
        try {
            int size = buffer.readUnsignedByte();
            String userName;
            String userPassword;
            for (int index = 0; index < size; index++) {
                userName = buffer.readString();
                userPassword = buffer.readString();
                accountList.add(new Account(userName, userPassword));
            }
        } catch (Exception e) {
            file.delete();
            e.printStackTrace();
        }
    }

}

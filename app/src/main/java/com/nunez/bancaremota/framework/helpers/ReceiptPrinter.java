package com.nunez.bancaremota.framework.helpers;

import android.content.Context;
import android.content.Intent;

import com.nunez.bancaremota.framework.respository.data.Game;
import com.nunez.bancaremota.screens.seller.sales.ticketBrief.TicketInfo;

import java.util.ArrayList;

public class ReceiptPrinter {
    private Context context;
    private TicketInfo ticketInfo;
    private ArrayList<Game> games;

    public ReceiptPrinter(Context context, TicketInfo ticketInfo, ArrayList<Game> games) {
        this.context = context;
        this.ticketInfo = ticketInfo;
        this.games = games;
        print();
    }

    void print() {
        ReceiptBuilder builder = new ReceiptBuilder()
                .setBeginningOfPage()
                .setTitle("Bancas Sport")
                .setDate(ticketInfo.getCreatedAt())
                .setReceiptNumber(ticketInfo.getNumber())
                .setGameHeader();

        for (Game game : games) {
            builder.setGame(game);
        }

        builder.setTotal(games)
                .setFooter();

        String receipt = builder.build();

        Intent intentPrint = new Intent();

        intentPrint.setAction(Intent.ACTION_SEND);
        intentPrint.putExtra(Intent.EXTRA_TEXT, receipt);
        intentPrint.setType("text/plain");

        context.startActivity(intentPrint);
    }

    private class ReceiptBuilder {
        private String receipt;
        private int totalSpaces = 32;

        ReceiptBuilder() {
            receipt = "";
        }

        ReceiptBuilder setBeginningOfPage() {
            this.receipt += "\n";
            return this;
        }

        ReceiptBuilder setTitle(String title) {
            this.receipt += "\n" + title;
            return this;
        }

        ReceiptBuilder setDate(String date) {
            this.receipt += "\nFecha: "+date;
            return this;
        }

        ReceiptBuilder setReceiptNumber(String number) {
            this.receipt += "\nNumero de ticket: \n" + getFormattedTicketNumber(number);
            return this;
        }

        ReceiptBuilder setGameHeader() {
            this.receipt += "\n\nJugadas";
            this.receipt += getLine();
            return this;
        }

        ReceiptBuilder setGame(Game game) {
            String line = game.getLottery_name() + " " + getFormattedGameNumbers(game);
            this.receipt += line;
            this.receipt += getFormattedAmount(line, String.valueOf(game.getAmount()));
            this.receipt += getLine();
            return this;
        }

        ReceiptBuilder setFooter() {
            this.receipt += "\n\n******** ¡Buena suerte! ********";
            this.receipt += "$intro$$cut$$intro$\n\n\n";
            return this;
        }


        ReceiptBuilder setTotal(ArrayList<Game> games) {
            Float totalAmount = 0f;

            for (Game game : games) {
                totalAmount += game.getAmount();
            }

            String total = "Total: $" + String.valueOf(totalAmount);

            int spacesToIndent = totalSpaces - total.length();
            this.receipt += "\n" + indentate(spacesToIndent) + total;

            return this;
        }

        String build() {
            return receipt;
        }

        private String getLine() {
            return "\n--------------------------------";
        }

        private String indentate(int spaces) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < spaces; i++) {
                sb.append(" ");
            }
            return sb.toString();
        }

        private String getFormattedAmount(String receipt, String amount) {
            int poundIndicatorLength = 1;
            int amountLength = amount.length();
            int spacesToIndent = totalSpaces - receipt.length() - poundIndicatorLength - amountLength;

            return indentate(spacesToIndent) + "$" + amount;
        }

        private String getFormattedTicketNumber(String ticketNumber) {
            int numberGroup = 4;
            StringBuilder formattedTicketNumber = new StringBuilder();
            char[] ticketNumberArray = ticketNumber.toCharArray();

            for (int i = 0; i < ticketNumber.length(); i++) {
                if (i != 0 && i % numberGroup == 0) {
                    formattedTicketNumber.append("-");
                }
                formattedTicketNumber.append(ticketNumberArray[i]);
            }
            return formattedTicketNumber.toString();
        }

        private String getFormattedGameNumbers(Game game) {
            String formattedGame = "";
            formattedGame += getNumberFormattedInTwoDigits(game.getFirst());
            formattedGame += getFormattedNumberIfNotNull(game.getSecond());
            formattedGame += getFormattedNumberIfNotNull(game.getThird());
            return formattedGame;
        }

        private String getFormattedNumberIfNotNull(Integer number) {
            String formatted = "";
            if (number != null) {
                formatted = "-" + getNumberFormattedInTwoDigits(number);
            }
            return formatted;
        }

        private String getNumberFormattedInTwoDigits(Integer number) {
            String formattedNumber = "";
            if(number < 10){
                formattedNumber = "0";
            }
            formattedNumber += String.valueOf(number);
            return formattedNumber;
        }
    }
}


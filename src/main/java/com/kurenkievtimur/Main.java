package com.kurenkievtimur;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Please input operation (encode/decode/exit):");
            String action = scanner.nextLine();

            switch (action) {
                case "encode" -> {
                    System.out.println("Input string:");
                    String input = scanner.nextLine();

                    String binary = encodeBinary(input);

                    String encode = encodeChuckNorrisCode(binary);
                    System.out.printf("Encoded string:\n%s\n\n", encode);
                }
                case "decode" -> {
                    System.out.println("Input encoded string:");
                    String encode = scanner.nextLine();

                    if (!isValidEncoded(encode)) {
                        System.out.println("Encoded string is not valid.\n");
                        continue;
                    }

                    String binary = decodeChuckNorrisCode(encode);

                    if (binary.length() % 7 != 0) {
                        System.out.println("Encoded string is not valid.\n");
                        continue;
                    }

                    String decode = decodeBinary(binary);
                    System.out.printf("Decoded string:\n%s\n\n", decode);
                }
                case "exit" -> {
                    System.out.println("Bye!");
                    return;
                }
                default -> System.out.printf("There is no '%s' operation\n\n", action);
            }
        }
    }

    public static String encodeBinary(String input) {
        StringBuilder builder = new StringBuilder();

        char[] chars = input.toCharArray();

        for (char c : chars) {
            String binary = "%7s".formatted(Integer.toBinaryString(c)).replace(" ", "0");

            builder.append(binary);
        }

        return builder.toString().trim();
    }

    public static String decodeBinary(String binary) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 7) {
            char ch = (char) Integer.parseInt(binary.substring(i, i + 7), 2);
            builder.append(ch);
        }

        return builder.toString();
    }

    public static String encodeChuckNorrisCode(String binary) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < binary.length(); i++) {
            StringBuilder block = new StringBuilder("0");
            StringBuilder blockIndicator = new StringBuilder("0");

            if (binary.charAt(i) == '0')
                blockIndicator.append('0');

            while (i < binary.length() - 1 && (binary.charAt(i) == binary.charAt(i + 1))) {
                block.append('0');
                i++;
            }

            builder
                    .append(blockIndicator)
                    .append(" ")
                    .append(block)
                    .append(" ");
        }

        return builder.toString();
    }

    public static String decodeChuckNorrisCode(String encode) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < encode.length(); i++) {
            if (encode.charAt(i) == ' ')
                continue;

            if (i < encode.length() - 1 && (encode.charAt(i) == '0' && encode.charAt(i + 1) == '0')) {
                builder.append("0");
                i += 3;

                while (i < encode.length() - 1 && (encode.charAt(i) == encode.charAt(i + 1))) {
                    builder.append("0");
                    i++;
                }
            } else if (i < encode.length() - 1 && (encode.charAt(i) == '0' && encode.charAt(i + 1) == ' ')) {
                builder.append("1");
                i += 2;

                while (i < encode.length() - 1 && (encode.charAt(i) == encode.charAt(i + 1))) {
                    builder.append("1");
                    i++;
                }
            }
        }

        return builder.toString();
    }

    public static boolean isValidEncoded(String encode) {
        if (encode.contains("1"))
            return false;

        int counterBlockIndicator = 0;

        for (int i = 0; i < encode.length(); i++) {
            StringBuilder blockIndicator = new StringBuilder("0");

            if (encode.indexOf(" 0", i + 1) != -1 && encode.charAt(i) == ' ')
                i = encode.indexOf(" 0", i + 1) + 1;

            while (encode.indexOf(" 0", i + 1) != -1 && encode.charAt(i + 1) != ' ') {
                blockIndicator.append("0");
                i++;
            }

            if (blockIndicator.length() > 2)
                return false;

            counterBlockIndicator++;
        }

        return counterBlockIndicator % 2 != 0;
    }
}
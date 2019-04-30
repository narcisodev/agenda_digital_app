package com.example.narcisogomes.myapplication.util;

public class Util {
    public static String transformaMes(int mes) {
        String mes_nome;
        switch (mes) {
            case 1:
                mes_nome = "01";
                break;
            case 2:
                mes_nome = "02";
                break;
            case 3:
                mes_nome = "03";
                break;
            case 4:
                mes_nome = "04";
                break;
            case 5:
                mes_nome = "05";
                break;
            case 6:
                mes_nome = "06";
                break;
            case 7:
                mes_nome = "07";
                break;
            case 8:
                mes_nome = "08";
                break;
            case 9:
                mes_nome = "09";
                break;
            case 10:
                mes_nome = "10";
                break;
            case 11:
                mes_nome = "11";
                break;
            case 12:
                mes_nome = "12";
                break;
            default:
                mes_nome = "Não Identificado";
                break;
        }
        return mes_nome;
    }

    public static String formataHora(int hora) {
        switch (hora) {
            case 0:
                return "00";
            case 1:
                return "01";
            case 2:
                return "02";
            case 3:
                return "03";
            case 4:
                return "04";
            case 5:
                return "05";
            case 6:
                return "06";
            case 7:
                return "07";
            case 8:
                return "08";
            case 9:
                return "09";
            default:
                return hora + "";
        }
    }


}

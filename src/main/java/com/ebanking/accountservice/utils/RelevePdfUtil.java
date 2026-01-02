package com.ebanking.accountservice.utils;

import com.ebanking.accountservice.dto.response.ReleveCompteDTO;
import com.ebanking.accountservice.dto.response.infoCompteDTO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RelevePdfUtil {

    /* =================== FONTS =================== */
    private static final Font TITLE_FONT =
            new Font(Font.HELVETICA, 16, Font.BOLD);

    private static final Font LABEL_FONT =
            new Font(Font.HELVETICA, 9, Font.BOLD);

    private static final Font TEXT_FONT =
            new Font(Font.HELVETICA, 9);

    private static final Font TABLE_HEADER_FONT =
            new Font(Font.HELVETICA, 9, Font.BOLD);

    private static final Font TOTAL_FONT =
            new Font(Font.HELVETICA, 10, Font.BOLD);

    /* =================== GENERATION PDF =================== */
    public byte[] generateRelevePdf(
            infoCompteDTO infoCompte,
            List<ReleveCompteDTO> transactions
    ) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 40, 40);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            /* =================== TITRE =================== */
            Paragraph title = new Paragraph("RELEVÉ DE COMPTE", TITLE_FONT);
            title.setAlignment(Element.ALIGN_LEFT);
            title.setSpacingAfter(20);
            document.add(title);

            /* =================== INFOS CLIENT =================== */
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{3, 2});

            PdfPCell left = new PdfPCell();
            left.setBorder(Rectangle.NO_BORDER);
            left.addElement(new Paragraph("Nom et prénom", LABEL_FONT));
            left.addElement(new Paragraph(
                    infoCompte.getPrenomClient() + " " + infoCompte.getNomClient(),
                    TEXT_FONT
            ));
//            left.addElement(new Paragraph("Marrakech", TEXT_FONT));
            left.addElement(Chunk.NEWLINE);
            left.addElement(new Paragraph("RIB", LABEL_FONT));
            left.addElement(new Paragraph(
                    infoCompte.getRipCompte(),
                    TEXT_FONT
            ));

            PdfPCell right = new PdfPCell();
            right.setBorder(Rectangle.NO_BORDER);
            right.setHorizontalAlignment(Element.ALIGN_RIGHT);
//            right.addElement(new Paragraph("Adresse et contact", LABEL_FONT));
//            right.addElement(new Paragraph("Organisme bancaire", TEXT_FONT));
//            right.addElement(Chunk.NEWLINE);
//            right.addElement(new Paragraph("Solde d’ouverture", LABEL_FONT));
//            right.addElement(new Paragraph(
//                    String.format("%.2f €", 1000.0),
//                    TOTAL_FONT
//            ));

            infoTable.addCell(left);
            infoTable.addCell(right);
            document.add(infoTable);

            document.add(Chunk.NEWLINE);

            /* =================== PERIODE =================== */
//            Paragraph periodeP = new Paragraph(
//                    "Du 01/01/2024 au 31/01/2024",
//                    TEXT_FONT
//            );
//            periodeP.setSpacingAfter(15);
//            document.add(periodeP);

            /* =================== TABLE TRANSACTIONS =================== */
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 2, 4, 2, 2});

            addHeader(table, "Date");
            addHeader(table, "Type");
            addHeader(table, "Description");
            addHeader(table, "Crédit (MAD)");
            addHeader(table, "Débit (MAD)");

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy");

            double totalCredit = 0.0;
            double totalDebit = 0.0;

//            for (ReleveCompteDTO t : transactions) {
//
//                table.addCell(new Phrase(
//                        t.getDate().format(formatter),
//                        TEXT_FONT
//                ));
//
//                table.addCell(new Phrase(
//                        t.getTypeTransaction(),
//                        TEXT_FONT
//                ));
//
//                table.addCell(new Phrase(
//                        t.getMotife() != null ? t.getMotife() : "",
//                        TEXT_FONT
//                ));
//
//                double montant = t.getMontant().doubleValue();
//
//                if (montant > 0) {
//                    table.addCell(new Phrase(
//                            String.format("%.2f", montant),
//                            TEXT_FONT
//                    ));
//                    table.addCell(new Phrase("", TEXT_FONT));
//                    totalCredit += montant;
//                } else {
//                    table.addCell(new Phrase("", TEXT_FONT));
//                    table.addCell(new Phrase(
//                            String.format("%.2f", Math.abs(montant)),
//                            TEXT_FONT
//                    ));
//                    totalDebit += Math.abs(montant);
//                }
//            }


            for (ReleveCompteDTO t : transactions) {

                PdfPCell c1 = new PdfPCell(new Phrase(
                        t.getDate().format(formatter), TEXT_FONT));
                c1.setPaddingTop(8f);
                c1.setPaddingBottom(8f);
                c1.setMinimumHeight(22f);
                table.addCell(c1);

                PdfPCell c2 = new PdfPCell(new Phrase(
                        t.getTypeTransaction()=="VIREMENT" ? t.getTypeTransaction() + " " + t.getTypeVirement() : t.getTypeTransaction(), TEXT_FONT));
                c2.setPaddingTop(8f);
                c2.setPaddingBottom(8f);
                c2.setMinimumHeight(22f);
                table.addCell(c2);

                PdfPCell c3 = new PdfPCell(new Phrase(
                        t.getMotife() != null ? t.getMotife() : "", TEXT_FONT));
                c3.setPaddingTop(8f);
                c3.setPaddingBottom(8f);
                c3.setMinimumHeight(22f);
                table.addCell(c3);

                double montant = t.getMontant().doubleValue();

                if (montant > 0) {
                    PdfPCell c4 = new PdfPCell(new Phrase(
                            String.format("%.2f", montant), TEXT_FONT));
                    c4.setPaddingTop(8f);
                    c4.setPaddingBottom(8f);
                    c4.setMinimumHeight(22f);
                    table.addCell(c4);

                    PdfPCell c5 = new PdfPCell(new Phrase("", TEXT_FONT));
                    c5.setPaddingTop(8f);
                    c5.setPaddingBottom(8f);
                    c5.setMinimumHeight(22f);
                    table.addCell(c5);
                    totalCredit += montant;
                } else {
                    PdfPCell c4 = new PdfPCell(new Phrase("", TEXT_FONT));
                    c4.setPaddingTop(8f);
                    c4.setPaddingBottom(8f);
                    c4.setMinimumHeight(22f);
                    table.addCell(c4);

                    PdfPCell c5 = new PdfPCell(new Phrase(
                            String.format("%.2f", Math.abs(montant)), TEXT_FONT));
                    c5.setPaddingTop(8f);
                    c5.setPaddingBottom(8f);
                    c5.setMinimumHeight(22f);
                    table.addCell(c5);
                    totalDebit += Math.abs(montant);
                }
            }


            document.add(table);
            document.add(Chunk.NEWLINE);

            /* =================== TOTAUX =================== */
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(40);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            totalTable.addCell(cellNoBorder("Total Crédit"));
            totalTable.addCell(cellNoBorder(
                    String.format("%.2f MAD", totalCredit)
            ));

            totalTable.addCell(cellNoBorder("Total Débit"));
            totalTable.addCell(cellNoBorder(
                    String.format("%.2f MAD", totalDebit)
            ));

            document.add(totalTable);
            document.add(Chunk.NEWLINE);

            /* =================== SOLDE FINAL =================== */
            Paragraph soldeFinalP = new Paragraph(
                    "Solde final : " +
                            String.format(
                                    "%.2f MAD",
                                    infoCompte.getBalance().doubleValue()
                            ),
                    TOTAL_FONT
            );
            soldeFinalP.setAlignment(Element.ALIGN_RIGHT);
            document.add(soldeFinalP);

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Erreur génération PDF", e);
        }

        return out.toByteArray();
    }

    /* =================== UTILS =================== */
    private void addHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, TABLE_HEADER_FONT));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(6);
        table.addCell(cell);
    }

    private PdfPCell cellNoBorder(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, TEXT_FONT));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }
}






//package com.ebanking.accountservice.utils;
//
//import com.ebanking.accountservice.dto.response.ReleveCompteDTO;
//import com.ebanking.accountservice.dto.response.TransactionDTO;
//import com.lowagie.text.*;
//import com.lowagie.text.Font;
//import com.lowagie.text.pdf.*;
//
//import java.awt.*;
//import java.io.ByteArrayOutputStream;
//import java.time.format.DateTimeFormatter;
//import java.util.List;   // ✅ BON IMPORT
//
//public class RelevePdfUtil {
//
//    private void addHeader(PdfPTable table, String text) {
//        Font font = new Font(Font.HELVETICA, 10, Font.BOLD);
//        PdfPCell cell = new PdfPCell(new Phrase(text, font));
//        cell.setBackgroundColor(Color.LIGHT_GRAY);
//        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.addCell(cell);
//    }
//
//    public byte[] generateRelevePdf(List<ReleveCompteDTO> transactions) {
//
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        Document document = new Document(PageSize.A4);
//
//        try {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//            PdfWriter.getInstance(document, out);
//            document.open();
//
//            // ===== Titre =====
//            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
//            Paragraph title = new Paragraph("Relevé de compte", titleFont);
//            title.setAlignment(Element.ALIGN_CENTER);
//            title.setSpacingAfter(20);
//            document.add(title);
//
//            // ===== Table =====
//            PdfPTable table = new PdfPTable(5);
//            table.setWidthPercentage(100);
//            table.setWidths(new float[]{2, 2, 4, 2, 2});
//
//            addHeader(table, "Date");
//            addHeader(table, "Type");
//            addHeader(table, "Description");
//            addHeader(table, "Crédit (MAD)");
//            addHeader(table, "Débit (MAD)");
//
//            for (ReleveCompteDTO t : transactions) {   //  OK maintenant
//                table.addCell(t.getDate().format(formatter));
//
//                if ("VIREMENT".equals(t.getTypeTransaction())) {
//                    table.addCell(t.getTypeTransaction() + " " + t.getTypeVirement());
//                    table.addCell(t.getMotife());
//                } else {
//                    table.addCell(t.getTypeTransaction());
//                    table.addCell("");
//                }
//
//
//                if (t.getMontant() > 0) {
//                    table.addCell(String.format("%.2f", t.getMontant()));
//                    table.addCell("");
//                } else {
//                    table.addCell("");
//                    table.addCell(String.format("%.2f", Math.abs(t.getMontant())));
//                }
//            }
//
//            document.add(table);
//            document.close();
//
//        } catch (Exception e) {
//            throw new RuntimeException("Erreur génération PDF", e);
//        }
//
//        return out.toByteArray();
//    }
//}





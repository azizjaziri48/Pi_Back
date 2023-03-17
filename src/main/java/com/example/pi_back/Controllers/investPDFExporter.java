package com.example.pi_back.Controllers;


import com.example.pi_back.Entities.Investment;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.List;

public class investPDFExporter {
	private List<Investment> listInvestment;

	public investPDFExporter(List<Investment> listInvestment) {
		this.listInvestment = listInvestment;
	}
	
	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
	//	cell.setBackgroundColor(Color.CYAN);
		
		//cell.setPadding(5);
		//Font font =FontFactory.getFont(FontFactory.HELVETICA);
		//font.setColor(Color.WHITE);
		
		
		cell.setPhrase(new Phrase("Amount"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Taux"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Invesstissor Email"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Invesstissor Name"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Invesstissor Second Name"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Invesstissor Profession"));
		table.addCell(cell);
	}
	
	private void writeTableData(PdfPTable table) {
		for (Investment Investment : listInvestment) {
			table.addCell(String.valueOf(Investment.getAmountInvestment()));
			table.addCell(String.valueOf(Investment.getTauxInves()));
			table.addCell(Investment.getMailInvestment());
			table.addCell(Investment.getNameInvestment());
			table.addCell(Investment.getSecondnameInvestment());
			table.addCell(Investment.getProfessionInvestment());
		}
	}
	
	/* void export(HttpServletResponse response) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document,response.getOutputStream());
		document.open();
		//Font font =FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		//font.setColor(Color.BLUE);
		//font.setSize(18);
		Paragraph title = new Paragraph("List of all investissement");
		document.add(title);
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		table.setSpacingBefore(15);
		writeTableHeader(table);
		writeTableData(table);
		document.add(table);
		document.close();
	}

	 */
}

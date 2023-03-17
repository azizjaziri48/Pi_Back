package com.example.pi_back.Controllers;


import com.example.pi_back.Entities.Project;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.List;

public class ProjectPDFExporter {
	private List<Project> listProj;

	public ProjectPDFExporter(List<Project> listInvestment) {
		this.listProj = listProj;
	}
	
	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
	//	cell.setBackgroundColor(Color.CYAN);
		
		//cell.setPadding(5);
		//Font font =FontFactory.getFont(FontFactory.HELVETICA);
		//font.setColor(Color.WHITE);
		
		
		cell.setPhrase(new Phrase("Amount"));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Project name "));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Description"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("TauxInvest"));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Final Amount"));
		table.addCell(cell);
	}
	
	private void writeTableData(PdfPTable table) {
		for (Project Project : listProj) {
			table.addCell(String.valueOf(Project.getAmountinvestment()));
			table.addCell(String.valueOf(Project.getTauxinvest()));
			table.addCell(Project.getDescription());
			table.addCell(Project.getName());
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

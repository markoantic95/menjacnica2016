package menjacnica.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;

import menjacnica.Menjacnica;
import menjacnica.MenjacnicaInterface;
import menjacnica.Valuta;
import menjacnica.gui.models.MenjacnicaTableModel;

public class GUIKontroler{

	private static MenjacnicaGUI prozor;
	private static MenjacnicaInterface menjacnica;
	private static MenjacnicaTableModel model;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					menjacnica = new Menjacnica();
					prozor = new MenjacnicaGUI();
					prozor.setVisible(true);
					prozor.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void ugasiAplikaciju() {
			int opcija = JOptionPane.showConfirmDialog(prozor.getContentPane(),
					"Da li ZAISTA zelite da izadjete iz apliacije", "Izlazak",
					JOptionPane.YES_NO_OPTION);

			if (opcija == JOptionPane.YES_OPTION)
				System.exit(0);
	}
	public static void prikaziAboutProzor(){
		JOptionPane.showMessageDialog(prozor.getContentPane(),
				"Autor: Bojan Tomic, Verzija 1.0", "O programu Menjacnica",
				JOptionPane.INFORMATION_MESSAGE);
	}
	public static void sacuvajUFajl() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(prozor.getContentPane());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				menjacnica.sacuvajUFajl(file.getAbsolutePath());
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(prozor.getContentPane(), e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void ucitajIzFajla(TableModel tableModel) {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(prozor.getContentPane());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				menjacnica.ucitajIzFajla(file.getAbsolutePath());
				prikaziSveValute(tableModel);
			}	
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(prozor.getContentPane(), e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	public static void prikaziSveValute(TableModel tableModel) {
		model = (MenjacnicaTableModel)(tableModel);
		model.staviSveValuteUModel(menjacnica.vratiKursnuListu());

	}
	public static JTable vratiTabelu(){
		return prozor.vratiTabelu();
	}
	
	public static void prikaziDodajKursGUI() {
		DodajKursGUI prozorDodaj = new DodajKursGUI();
		prozorDodaj.setLocationRelativeTo(prozor.getContentPane());
		prozorDodaj.setVisible(true);
	}
	public static void prikaziObrisiKursGUI(JTable table) {
		
		if (table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel)(table.getModel());
			ObrisiKursGUI prozorObrisi = new ObrisiKursGUI(
					model.vratiValutu(table.getSelectedRow()));
			prozorObrisi.setLocationRelativeTo(prozor.getContentPane());
			prozorObrisi.setVisible(true);
		}
	}
	
	public static void prikaziIzvrsiZamenuGUI(JTable table) {
		if (table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel)(table.getModel());
			IzvrsiZamenuGUI prozorIzvrsi = new IzvrsiZamenuGUI(
					model.vratiValutu(table.getSelectedRow()));
			prozorIzvrsi.setLocationRelativeTo(prozor.getContentPane());
			prozorIzvrsi.setVisible(true);
		}
	}
	public static void unesiKurs(String naziv,String skracenica,double prodajni,double kupovni,double srednji,int sifra) {
		try {
			Valuta valuta = new Valuta();

			// Punjenje podataka o valuti
			valuta.setNaziv(naziv);
			valuta.setSkraceniNaziv(skracenica);
			valuta.setSifra(sifra);
			valuta.setProdajni(prodajni);
			valuta.setKupovni(kupovni);
			valuta.setSrednji(srednji);
			
			// Dodavanje valute u kursnu listu
			menjacnica.dodajValutu(valuta);

			// Osvezavanje glavnog prozora
			prikaziSveValute(vratiTabelu().getModel());
			
			//Zatvaranje DodajValutuGUI prozora
			
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	public static void prikaziValutu(JTextField textFieldProdajniKurs, JTextField textFieldKupovniKurs,
			JTextField textFieldValuta,Valuta valuta){
		textFieldProdajniKurs.setText(""+valuta.getProdajni());
		textFieldKupovniKurs.setText(""+valuta.getKupovni());
		textFieldValuta.setText(valuta.getSkraceniNaziv());
	}
	
	public static void izvrsiZamenu(Valuta valuta, JTextField textFieldIznos, JTextField textFieldKonacniIznos, JRadioButton rdbtnProdaja){
		try{
			double konacniIznos = 
					menjacnica.izvrsiTransakciju(valuta,
							rdbtnProdaja.isSelected(), 
							Double.parseDouble(textFieldIznos.getText()));
		
			textFieldKonacniIznos.setText(""+konacniIznos);
		} catch (Exception e1) {
		JOptionPane.showMessageDialog(null, e1.getMessage(),
				"Greska", JOptionPane.ERROR_MESSAGE);
	}
	}
	public static void prikaziValutu(Valuta valuta,JTextField textFieldNaziv,JTextField textFieldSkraceniNaziv,
			JTextField textFieldSifra,JTextField textFieldProdajniKurs,JTextField textFieldKupovniKurs,JTextField textFieldSrednjiKurs) {
		// Prikaz podataka o valuti
		textFieldNaziv.setText(valuta.getNaziv());
		textFieldSkraceniNaziv.setText(valuta.getSkraceniNaziv());
		textFieldSifra.setText(""+valuta.getSifra());
		textFieldProdajniKurs.setText(""+valuta.getProdajni());
		textFieldKupovniKurs.setText(""+valuta.getKupovni());
		textFieldSrednjiKurs.setText(""+valuta.getSrednji());				
	}

	public static void obrisiValutu(Valuta valuta) {
		try{
			menjacnica.obrisiValutu(valuta);
			
			prikaziSveValute(vratiTabelu().getModel());
			
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
}

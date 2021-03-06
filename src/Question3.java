import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Question3 {

	private Map<String, String> vecteur1 = new HashMap<String, String>();
	private Map<String, String> vecteur2 = new HashMap<String, String>();
	
	/**
	 * methode qui permet de parcourir le repertoire ou il y a tout les fichiers pdf (offres de stage)
	 */
	public void parcourirRepertoirePDF(){
		File dir = new File("offresStage");
		
		File[] offres = dir.listFiles();
		
		if (offres == null) {
			System.out.println("le repertoire est vide");
		}else{
			
			for(int i = 0; i < offres.length; i++){
				obtenirContanuPDF(offres[i].getPath());
			}
			
		}
	}
	
	
	
	/**
	 * obtenir le contenu (texte) de chaque fichier pdf pour faire la comparaison
	 */
	public void obtenirContanuPDF(String path){
		String contenu = "";
		
		File file = new File(path);
		try {
			PDDocument doc = PDDocument.load(file);
			contenu = new PDFTextStripper().getText(doc);
			doc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		contenu.toLowerCase();
		remplirV1(contenu, file.getName());
	}
	
	/**
	 * remplire v1 et v2 avec tout les fichiers (nom et contenu)
	 */
	public void remplirV1(String contenu, String fichier){
		vecteur1.put(fichier, contenu);
		vecteur2.put(fichier, contenu);
	}
	
	/**
	 * on parcour les 2 vecteurs V1 et V2, dans le vecteur 2 on efface les offres de stage qui sont en doublons
	 * a la fin du traitement le V2 contiendra les fichiers sans doublons
	 * @throws IOException 
	 */
	public void trouverEffacerDoublons() throws IOException{
		int nbDublons = 0;
		String contenuAux = "";
		int i = 0;
		int x = 0;
		int test = 0;
	
		File ff=new File("SansDoublons.txt"); // d�finir l'arborescence
		ff.createNewFile();
		FileWriter ffw=new FileWriter(ff);
		
		File ff1=new File("doublons.txt"); // d�finir l'arborescence
		ff1.createNewFile();
		FileWriter ffw1 =new FileWriter(ff1);
		
		
		
		for(Map.Entry<String, String> m : vecteur1.entrySet()){
			contenuAux = m.getValue();
			
			Iterator it = vecteur2.entrySet().iterator();
			while (it.hasNext()) {
				test ++;
				Map.Entry<String, String> entry = (Entry<String, String>) it.next();
				//int distance = levenshteinDistance(contenuAux, entry.getValue());
				System.out.println(test);
				//if( distance == 0){
				if(entry.getValue().equals(contenuAux)){
					i++;
					
					if(i > 1){
						
						nbDublons++;
						ffw1.write(entry.getKey());
						ffw1.write("\n");
						it.remove();
					}else{
						ffw.write(entry.getKey());
						ffw.write("\n");
					}
				}
				
			}
			
			i = 0;
			
				
		}
		
		ffw.close();
		ffw1.close();
		System.out.println("Taille vecteur d'origine: " + vecteur1.size());
		System.out.println("Taille vecteur sans doublons: " + vecteur2.size());
		System.out.println("Numero de doublons doublons: " + nbDublons);
		
	}
	
	/**
	 * calculer la distance entre les contenus de pdf (distance de lenvenshtein)
	 * si la distance est egale a 0, �a veut dire que le fichiers sont des doublons
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static Integer levenshteinDistance (CharSequence lhs, CharSequence rhs) {
	
	    int len0 = lhs.length() + 1;
	    int len1 = rhs.length() + 1;

	    // the array of distances
	    int[] cost = new int[len0];
	    int[] newcost = new int[len0];

	    // initial cost of skipping prefix in String s0
	    for (int i = 0; i < len0; i++) cost[i] = i;

	    // dynamically computing the array of distances

	    // transformation cost for each letter in s1
	    for (int j = 1; j < len1; j++) {
	        // initial cost of skipping prefix in String s1
	        newcost[0] = j;

	        // transformation cost for each letter in s0
	        for(int i = 1; i < len0; i++) {
	            // matching current letters in both strings
	            int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

	            // computing cost for each transformation
	            int cost_replace = cost[i - 1] + match;
	            int cost_insert  = cost[i] + 1;
	            int cost_delete  = newcost[i - 1] + 1;

	            // keep minimum cost
	            newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
	        }

	        // swap cost/newcost arrays
	        int[] swap = cost; cost = newcost; newcost = swap;
	    }

	    // the distance is the cost for transforming all letters in both strings
	    return cost[len0 - 1];
	}

}

package fr.arty.seventhsea.test;
import java.util.ArrayList;
import java.util.List;

import fr.arty.seventhsea.entity.DicesSet;
import fr.arty.seventhsea.entity.Raise;
import fr.arty.seventhsea.util.CalculationUtils;

/**
 * Classe de tests.
 * @author Arty
 *
 */
public class TestCalculation {
	
	/** nombre de dés à générer. Changer cette valeur pour modifier le nombre de dés à tester. */
	private static final int NUMBER_OF_DICES = 20;
	
	/** Liste des dés générés. */
	private static DicesSet dices;
	
	/** mises valides. */
	private static List<Raise> raises = new ArrayList<Raise>();

	
	public static void main(String[] args) {
		dices = new DicesSet(CalculationUtils.generateRandomDiceSet(NUMBER_OF_DICES));
		int max = dices.total() / dices.getType().threshold();
		
		System.out.println("Liste des dés " + dices);
		System.out.println("Nb de mises théoriques max : " + max);
		System.out.println("Nb de dés : " + dices.dicesSum());
		
		for (int i = 0; i < max; i++) {
			raises.add(CalculationUtils.getARaise(dices));
		}

		for (Raise raise : raises) {
			if (raise != null) {
				System.out.println("Mise : " + raise);
			}
		}
		System.out.println("Inutilisés : " + dices);
	}
}

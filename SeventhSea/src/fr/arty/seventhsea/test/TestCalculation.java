package fr.arty.seventhsea.test;
import java.util.ArrayList;
import java.util.List;

import fr.arty.seventhsea.entity.DicesSet;
import fr.arty.seventhsea.entity.Raise;
import fr.arty.seventhsea.util.CalculationUtils;

public class TestCalculation {
	private static DicesSet dices;
	private static List<Raise> raises = new ArrayList<Raise>();

	public static void main(String[] args) {
		dices = new DicesSet(CalculationUtils.generateRandomDiceSet(20));
		int max = dices.total() / 10;
		
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

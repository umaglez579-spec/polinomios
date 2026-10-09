package aed.polinomios;

import java.util.Arrays;
import java.util.function.BiFunction;

import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.*;


/**
 * Operaciones sobre polinomios de una variable con coeficientes enteros.
 */
public class Polinomio {

  // Una lista de monomios -- NO CAMBIA a private; el tester necesita acceso
  PositionList<Monomio> terms;

  /**
   * Crea un polinomio representando el valor 0.
   */
  public Polinomio() {
    terms = new NodePositionList<>();
  }

  /**
   * Crea un polinomio definado por una lista con monomios.
   * @param terms una lista de monomios
   */
  public Polinomio(PositionList<Monomio> terms) {
    this.terms = terms;
  }

  /**
   * Crea un polinomio definado por un String.
   * La representación del polinomio es una secuencia de monomios separados
   * por '+' (y posiblemente con caracteres blancos).
   * Un monomio esta compuesto por tres partes,
   * el coeficiente (un entero), el caracter 'x' (el variable), y el exponente
   * compuesto por un un caracter '^' seguido por un entero.
   * Se puede omitir multiples partes de un monomio, 
   * ejemplos:
   * <pre>
   * {@code
   * new Polinomio("2x^3 + 9");
   * new Polinomio("2x^3 + -9");
   * new Polinomio("x");   // == 1x^1
   * new Polinomio("5");   // == 5x^0
   * new Polinomio("8x");  // == 8x^1
   * new Polinomio("0");   // == 0x^0
   * }
   * </pre>
   * @throws IllegalArgumentException si el argumento es malformado
   * @param polinomio - una secuencia de monomios separados por '+'
   */
  public Polinomio(String polinomio) throws IllegalArgumentException{
      throw new RuntimeException("no esta implementado todavia");
  }

  public static Monomio sum(Monomio uno, Monomio dos){
    return new Monomio(uno.coeficiente() + dos.coeficiente(), uno.exponente());
  }

  public static Monomio rest(Monomio uno, Monomio dos){
    return new Monomio(uno.coeficiente() - dos.coeficiente(), uno.exponente());
  }

  public static Polinomio colocar(Polinomio p1, Polinomio p2, int a){
    Position<Monomio> punt1 = p1.terms.first();
    Position<Monomio> punt2 = p2.terms.first();
    PositionList<Monomio> res = new NodePositionList<Monomio>();
    Position<Monomio> puntRes = res.first();
    if(punt1.element().exponente() < punt2.element().exponente()){
      res.addBefore(punt2, punt1.element());
      punt2 = p2.terms.next(punt2);
      puntRes = res.next(puntRes);
    }
    else if(punt2.element().exponente() < punt1.element().exponente()){
      res.addBefore(punt1, punt2.element());
      punt1 = p1.terms.next(punt1);
      puntRes = res.next(puntRes);
    }
    else{
      if(a > 0)
        res.set(puntRes, sum(punt1.element(), punt2.element()));
      else
        res.set(puntRes, rest(punt1.element(), punt2.element()));
      puntRes = res.next(puntRes);
    }
    return new Polinomio(res);
  }
  /**
   * Suma dos polinomios.
   * @param p1 primer polinomio.
   * @param p2 segundo polinomio.
   * @return la suma de los polinomios.
   */
  public static Polinomio suma(Polinomio p1, Polinomio p2) {
    int a = 1;
    return colocar(p1, p2, a);
  }
    
  /**
   * Substraccion de dos polinomios.
   * @param p1 primer polinomio.
   * @param p2 segundo polinomio.
   * @return la resta de los polinomios.
   */
  public static Polinomio resta(Polinomio p1, Polinomio p2) {
    int a = -1;
    return colocar(p1, p2, a);
  }
    
  /**
   * Calcula el producto de dos polinomios.
   * @param p1 primer polinomio.
   * @param p2 segundo polinomio.
   * @return el producto de los polinomios.
   */
  public static Polinomio multiplica(Polinomio p1, Polinomio p2) {
    return null;
  }

  /**
   * Calcula el producto de un monomio y un polinomio.
   * @param m el monomio
   * @param p el polinomio
   * @return el producto del monomio y el polinomio
   */
  public static Polinomio multiplica(Monomio m, Polinomio p) {
    return null;
  }
    
  /**
   * Devuelve el exponente (grado) del monomio con el mayor grado
   * dentro del polinomio
   * Si el polinomio es vacio (la representacion del polinomio "0") entonces
   * el valor devuelto debe ser -1.
   * @return el grado del polinomio
   */
  public int grado() {
    return -1;
  }

  /**
   * Cambia las occurrencias del variable "x" dentro el polinomio p por el polinomio arg.
   * @return El polinomio p donde el variable "x" esta sustuido por el polinomio arg.
   * Ejemplo:
   * <pre>
   * {@code
   * Polinomio p1 = new Polinomio("2x^3 + 9");
   * Polinomio p2 = new Polinomio("x + 1");
   * Polinomio.componer(p1,p2) =>
   *   2 * (x+1)^3 + 9 = 2x^3 + 6x^2 + 6x^1 + 11
   * }
   * </pre>
   *
   */
  public static Polinomio componer(Polinomio p, Polinomio arg) {
    return null;
  }

  /**
   * Calcula el derivado de un polinomio.
   * @return el polinomio representando el derivado.
   */
  public Polinomio derivada() {
    return null;
  }

  @Override
  public int hashCode() {
    return terms.hashCode();
  }

  @Override
  public String toString() {
    if (terms.isEmpty()) return "0";
    else {
      StringBuffer buf = new StringBuffer();
      Position<Monomio> cursor = terms.first();
      while (cursor != null) {
        Monomio p = cursor.element();
        int coef = p.coeficiente();
        int exp = p.exponente();
        buf.append(new Integer(coef).toString());
        if (exp > 0) {
          buf.append("x");
          buf.append("^");
          buf.append(new Integer(exp)).toString();
        }
        cursor = terms.next(cursor);
        if (cursor != null) buf.append(" + ");
      }
      return buf.toString();
    }
  }
}

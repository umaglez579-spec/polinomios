package aed.polinomios;

import java.util.Arrays;
import java.util.function.BiFunction;

import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.*;
import es.upm.aedlib.set.Set;


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
   * el coefficiente (un entero), el caracter 'x' (el variable), y el exponente
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
  public Polinomio(String polinomio) {
	  int coe = 0; // coeficiente
	  int exp = 0; // exponente
	  int posicion;
	  String sinEspacios = polinomio.replace(" ", ""); // eliminar todos los espacios
	  String[] monomios = sinEspacios.split("\\+"); // array de Strings dividido por los símbolos "+"
	  String monomio; 
	  String parte; // exponente y coeficiente
	  PositionList<Monomio> resultado = new NodePositionList<Monomio>();
	  for(int i = 0; i < monomios.length; i++) {
		 monomio = monomios[i];
		 if (monomio.contains("x")) {
			posicion = monomio.indexOf("x");
			parte = monomio.substring(0, posicion); // monomio desde 0 hasta posición
			if (parte.isEmpty()) {
				coe = 1; // si está vacío el coeficiente de la x es 1
			} else {
				coe = Integer.parseInt(parte);	
			}
		 } else { // no contiene x
			 coe = Integer.parseInt(monomio);	
		 }
		 if(monomio.contains("^")) { // monomio elevado a algún número
			 posicion = monomio.indexOf("^");
		     parte = monomio.substring(posicion+1); 
		     exp = Integer.parseInt(parte);
		 }else {
			 if(monomio.contains("x")) { // contener x elevado a 1
				 exp=1;
			 }else {
			 exp=0;
			 }
		 }
		 if(coe != 0 || exp != 0) {
		 resultado.addLast(new Monomio(coe,exp));
		 }
		 
	  }
	  this.terms=resultado;
  }

  /**
   * Suma dos polinomios.
   * @param p1 primer polinomio.
   * @param p2 segundo polinomio.
   * @return la suma de los polinomios.
   */
  private static Polinomio sumRes (Polinomio p1, Polinomio p2) {
	  if(p1.terms.isEmpty()) { // si uno está vacío devolver el otro
			 return  new Polinomio(new NodePositionList<Monomio>(p2.terms));
		  }
		  if(p2.terms.isEmpty()) {
			 return  new Polinomio(new NodePositionList<Monomio>(p1.terms));
		  }
		  Position<Monomio> puntero1= p1.terms.first();
		  Position<Monomio> puntero2= p2.terms.first();
		  PositionList<Monomio> resultado=new NodePositionList<Monomio>(p1.terms);
		  Position<Monomio> punteroR= resultado.first();
		  
		  for(int i=0;i<p2.terms.size();i++) {
			  
			  if(puntero1.element().exponente() > puntero2.element().exponente()) {
				  if((p1.terms.next(puntero1)) == null) { // si ya no hay más elementos de p1 añadir todo p2 a continuación
					  resultado.addAfter(punteroR, puntero2.element());
					  puntero2 = p2.terms.next(puntero2);
					  punteroR = resultado.next(punteroR);
				  }else {
					 puntero1=p1.terms.next(puntero1); 
					 punteroR=resultado.next(punteroR);
					 i--; 
				  }
				  continue;
			  }
			  
			  if(puntero1.element().exponente() == puntero2.element().exponente()) {
				  Monomio añadir=new Monomio(puntero1.element().coeficiente()+(puntero2.element().coeficiente()), puntero1.element().exponente());
				  resultado.set(punteroR, añadir); // cambiar valor de punteroR por el valor "añadir"	  
				  puntero2=p2.terms.next(puntero2); 
				  if(punteroR.element().coeficiente()==0) { // si "añadir" tiene coef = 0 eliminarlo
					  if(punteroR.equals(resultado.first())){
					  resultado.remove(punteroR); 
					  punteroR=resultado.first(); // volver al comienzo
					  puntero1=p1.terms.next(puntero1); 
					  continue;
					  }else { // punteroR no está en la primera posición
					  punteroR = resultado.prev(punteroR); 
					  Position<Monomio> punteroA= resultado.next(punteroR); // eliminar el valor del puntero auxiliar
					  resultado.remove(punteroA);
					  }
				  }
				  if(punteroR == resultado.last()) {
					  continue;
				  }
				  puntero1=p1.terms.next(puntero1); 
				  punteroR=resultado.next(punteroR);
				 
				  continue;
			  }
			  
			  if(puntero1.element().exponente() < puntero2.element().exponente()) {
				  resultado.addBefore(punteroR, puntero2.element()); // añadir elemento de p2 antes que p1 si su exponente es mayor
				  puntero2 = p2.terms.next(puntero2); 
			  }
		  }  
	    return new Polinomio(resultado);
  }
  
  public static Polinomio suma(Polinomio p1, Polinomio p2) {
	  return sumRes(p1,p2);
	    }
  /**
   * Substraccion de dos polinomios.
   * @param p1 primer polinomio.
   * @param p2 segundo polinomio.
   * @return la resta de los polinomios.
   */
  
  public static Polinomio resta(Polinomio p1, Polinomio p2) {
	  Polinomio p2A=multiplica(new Monomio(-1,0), p2); // sumar inverso de p2
    return sumRes(p1,p2A);
  }
    
  /**
   * Calcula el producto de dos polinomios.
   * @param p1 primer polinomio.
   * @param p2 segundo polinomio.
   * @return el producto de los polinomios.
   */
  public static Polinomio multiplica(Polinomio p1, Polinomio p2) {
	  Position<Monomio> puntero= p2.terms.first();
	  Polinomio solucion=new Polinomio();
	  for(int i=0;i<p2.terms.size();i++) {
		  solucion=sumRes(solucion,multiplica(puntero.element(),p1)); // multiplica monomio(puntero) por cada elem de p1
		  puntero=p2.terms.next(puntero);
	  }
    return solucion;
  }

  /**
   * Calcula el producto de un monomio y un polinomio.
   * @param m el monomio
   * @param p el polinomio
   * @return el producto del monomio y el polinomio
   */
  public static Polinomio multiplica(Monomio m, Polinomio p) {
	  if(m.coeficiente() == 0 && m.exponente() == 0 || p.terms.isEmpty()) {
		  return new Polinomio(); // Uno de los terminos 0 = polinomio vacío
	  }
	  Position<Monomio> puntero = p.terms.first();
	  PositionList<Monomio> resultado = new NodePositionList<Monomio>();
	  for (int i = 0; i < p.terms.size(); i++) {
		  Monomio añadir = new Monomio(puntero.element().coeficiente()*(m.coeficiente()), puntero.element().exponente() + (m.exponente()));
		  resultado.addLast(añadir); // añadir nuevo monomio creado al final para evitar punteros
		  puntero = p.terms.next(puntero);
	  }
    return new Polinomio(resultado);
  }
    
  /**
   * Devuelve el exponente (grado) del monomio con el mayor grado
   * dentro del polinomio
   * Si el polinomio es vacio (la representacion del polinomio "0") entonces
   * el valor devuelto debe ser -1.
   * @return el grado del polinomio
   */
  public int grado() {
	  if(terms.isEmpty()) {
		return -1;  
	  }  
	  Position<Monomio> puntero = terms.first();
	  return puntero.element().exponente(); // el grado mayor siempre va a ser el primer elemento de terms
  }

  
  
  private static Polinomio elevar(Polinomio p, int a) {
	  if(a == 0) { // x^0 = 1
		  return new Polinomio(new NodePositionList<Monomio>(new Monomio[] { new Monomio(1,0)}));
	  }
	  Polinomio solucion=new Polinomio(p.terms);
	  for(int i=1;i<a;i++) { // multiplicarlo por sí mismo a veces
		  solucion=multiplica(solucion,p);
	  }
	return solucion;
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
	  Position<Monomio> puntero= p.terms.first();
	  Polinomio solucion=new Polinomio();
	  for(int i=0;i<p.terms.size();i++) {
		  Monomio auxiliar=new Monomio(puntero.element().coeficiente(), 0); // tomar solo el coef
		  // elevar arg al exp del puntero, multiplicarlo por el coef del puntero y sumarlo a solución
		  solucion=sumRes(solucion,multiplica(auxiliar,elevar(arg,puntero.element().exponente()))); 
		  puntero=p.terms.next(puntero);
	  }
    return solucion;
  }

  /**
   * Calcula el derivado de un polinomio.
   * @return el polinomio representando el derivado.
   */
  public Polinomio derivada() {
	  PositionList<Monomio> resultado = new NodePositionList<Monomio>();
	  Position<Monomio> puntero = terms.first();
	  for(int i = 0; i < terms.size(); i++) {
		  if(puntero.element().exponente() != 0) { // si la variable no está elevada a 0 se deriva
			  // el nuevo monomio se crea con su coeficiente*exponenteDelOriginal, exponente-1
			  resultado.addLast(new Monomio(puntero.element().coeficiente()*puntero.element().exponente(), puntero.element().exponente() - 1));
			  puntero = terms.next(puntero);
		  }  
	  }
    return new Polinomio(resultado);
  }
  
  @Override
  public boolean equals(Object obj) {
	  if(!(obj instanceof Polinomio)) // comprobar si puede ser un Polinomio
		  return false;
	  Polinomio p = (Polinomio) obj; // conversión explícita
	  return this.terms.equals(p.terms);
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

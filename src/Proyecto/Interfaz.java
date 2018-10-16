package Proyecto;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class Interfaz extends JFrame implements ActionListener, CaretListener, DocumentListener{
	

	private static final long serialVersionUID = 1L;
	
	JMenuBar Menu;
	JMenu MenuArchivo;
	JMenuItem elementoNuevo;
	JMenuItem elementoAbrir;
	JMenuItem elementoGuardar;
	JMenuItem elementoGuardarComo;
	JMenuItem elementoSalir;
	
	JScrollPane Prog;
	JScrollPane Res;
	JScrollPane RPar;
	
	
	
	JTextArea Programa;
	JTextArea Resultado;
	JTextArea ResParser;
	
	
	
	JLabel lbPrograma;
	JLabel lbResultado;
	JLabel lbParser;
	JLabel lbTotal;
        JLabel lbEscanear;
        JLabel lbLogo;
	
	

	JButton Calcular;

	boolean guarda = false;
	String NombreDoc;
	
	int ancho;
	int alto;
	
	public static String TokenTipo;

	
	Vector <Tokens> VectorTokens = new Vector <Tokens> (20,1);
	
	//Fila y Columna
	JTextField FilaColumna;
	int linea;
	int columna;
	
	Analizador	ObjAnalizador;
	ParserClass ObjParser;
	
	//Primero scaneo luego parser
	boolean Escaneado = false;
	
	public Interfaz(){
		super("Compilador");
		        this.getContentPane().setBackground(Color.cyan);

		Ventana();
		Escuchas();
		
		//Es para cambiar la resolucion sin tener que modificar todas las posiciones
		addComponentListener(new java.awt.event.ComponentAdapter() {
	         public void componentResized(java.awt.event.ComponentEvent evt) {
	     		ancho = getWidth();
	    		alto  = getHeight();
	    		
	    		Prog.setSize			(((int)(ancho/3.4)),((int)(alto/1.80)));
	    		Res.setSize				(((int)(ancho/3.4)),((int)(alto/1.80)));
	    		RPar.setSize			(((int)(ancho/3.4)),((int)(alto/1.80)));
	    		
	    		
	    		
	    		lbPrograma.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
	    		lbResultado.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
	    		lbParser.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
	    		
	    		//Tamaño de la imagen
	    		Calcular.setSize		(((int)(ancho/12.5)),((int)(alto/14.5)));
	    		
	    		lbTotal.setSize			(((int)(ancho/4)),((int)(alto/14)));
	    		FilaColumna.setSize		(((int)(ancho/3.0188)),((int)(alto/17.5)));
	    		
	    		lbPrograma.setLocation	(((int)(ancho/12)),((int)(alto/6)));
	    		lbResultado.setLocation	(((int)(ancho/1.30)),((int)(alto/6)));
	    		lbParser.setLocation	(((int)(ancho/2.27)),((int)(alto/6)));
	    		
	    		
	    		Prog.setLocation		(((int)(ancho/160)),((int)(alto/4)));
	    		Res.setLocation			(((int)(ancho/3)),((int)(alto/4)));
                        RPar.setLocation		(((int)(ancho/1.5)),((int)(alto/4)));
	    		
	    		
	    		Calcular.setLocation	(((int)(ancho/2.0)),((int)(alto/20)));		
	    		lbTotal.setLocation		(((int)(ancho/3)),((int)(alto/1.2727)));
	  
	    		FilaColumna.setLocation	(((int)(ancho/20)),((int)(alto/1.2727)));   		
	    		Calcular.setIcon((new ImageIcon(((new ImageIcon("analyze.png")).getImage()).getScaledInstance
	    				(Calcular.getWidth(), Calcular.getHeight(), java.awt.Image.SCALE_SMOOTH))));
                      
	    		validate();
	         }
	     });
		
	}
	
	public void Escuchas(){
		 elementoNuevo.addActionListener(this);
		 elementoAbrir.addActionListener(this);
		 elementoGuardar.addActionListener(this);
		 elementoGuardarComo.addActionListener(this);
		 elementoSalir.addActionListener(this);
		 Calcular.addActionListener(this);
		 
		 Programa.addCaretListener(this);
		 
		 Programa.getDocument().addDocumentListener(this);
	}
	
	public void Ventana(){
		setSize(900,600);
		setLocationRelativeTo(null);
		setLayout(null);
		ancho = getWidth();
		alto  = getHeight();
		this.setIconImage(new ImageIcon("OW.png").getImage());
		this.setResizable(false);
		
		
		
		
		//Barra de menu y archivo
		Menu = new JMenuBar();
		
		MenuArchivo = new JMenu( "Archivo" ); // Crea el menu archivo
	    MenuArchivo.setMnemonic( 'A' ); // Establece el nemónico a A
		
	    //Opción Nuevo
	    elementoNuevo = new JMenuItem( "Nuevo Archivo" );
	    elementoNuevo.setMnemonic( 'N' );
	    MenuArchivo.add( elementoNuevo );
	    
	    //Opción Abrir
	    elementoAbrir = new JMenuItem( "Abrir Archivo" ); // crea el elemento abrir
	    elementoAbrir.setMnemonic( 'A' );
	    MenuArchivo.add( elementoAbrir );
		
	    //Opción Guardar
            elementoGuardar = new JMenuItem( "Guardar Archivo" );
            elementoGuardar.setMnemonic( 'G' );
            MenuArchivo.add( elementoGuardar );
		
            //Opción Guardar Como
            elementoGuardarComo = new JMenuItem( "Guardar Archivo Como" );
            elementoGuardarComo.setMnemonic( 'C' );
            MenuArchivo.add( elementoGuardarComo );
		
            //Opción Salir
            elementoSalir = new JMenuItem( "Salir" );
            elementoSalir.setMnemonic( 'S' );
            MenuArchivo.add( elementoSalir );
	    
            Menu.add( MenuArchivo );
            setJMenuBar( Menu ); // Agrega la barra de menus a la aplicación
		
		
		//Crear elementos
		//Crear objetos
		Programa = new JTextArea();
		Resultado = new JTextArea();
		ResParser = new JTextArea();
		
			
		Prog = new JScrollPane(Programa);
		Res = new JScrollPane(Resultado);
		RPar = new JScrollPane(ResParser);
		
		
		TextLineNumber tln = new TextLineNumber(Programa);
		Prog.setRowHeaderView( tln );
		
                lbEscanear = new JLabel("Escanear");
                lbLogo = new JLabel(); 
                
                      
		lbPrograma = new JLabel("Programa");
		lbResultado = new JLabel("Parser");
		lbParser = new JLabel("Escaner");
		lbTotal = new JLabel ("# de Tokens = ");
                
		
		
		Calcular = new JButton();
		
		FilaColumna = new JTextField("Fila: 0 \t Columna: 0");
		
		//Tamañnos
		Prog.setSize			(((int)(ancho/1.1)),((int)(alto/1.48)));
		Res.setSize				(((int)(ancho/2.4)),((int)(alto/3)));
		//Nuevos resultados del parser
		RPar.setSize			(((int)(ancho/2.4)),((int)(alto/3)));
		
		lbPrograma.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
		lbResultado.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
		lbParser.setSize		(((int)(ancho/2.2857)),((int)(alto/14)));
                lbEscanear.setSize		(((int)(ancho/2.2727)),((int)(alto/14)));
		
		Calcular.setSize		(((int)(ancho/6.5)),((int)(alto/17.5)));
                lbLogo.setSize		(((int)(ancho/5.5)),((int)(alto/5.5)));
		
		lbTotal.setSize			(((int)(ancho/60)),((int)(alto/50)));
		FilaColumna.setSize		(((int)(ancho/3.0188)),((int)(alto/17.5)));

		//Posiciones
		lbPrograma.setLocation	(((int)(ancho/28)),((int)(alto/20)));
		lbResultado.setLocation	(((int)(ancho/1.8)),((int)(alto/20)));
		lbParser.setLocation	(((int)(ancho/1.8)),((int)(alto/3.16)));
                lbEscanear.setLocation	(((int)(ancho/1.70)),((int)(alto/20)));
		
		Prog.setLocation		(((int)(ancho/160)),((int)(alto/9)));
		Res.setLocation			(((int)(ancho/1.9)),((int)(alto/9)));
		
		//Nuevo resultados del parser
		RPar.setLocation		(((int)(ancho/1.9)),((int)(alto/1.9)));
		Calcular.setLocation	(((int)(ancho/160)),((int)(alto/350)));
                lbLogo.setLocation	(((int)(ancho/14)),((int)(alto/60)));
		lbTotal.setLocation		(((int)(ancho/1.4)),((int)(alto/2.35)));
		FilaColumna.setLocation	(((int)(ancho/20)),((int)(alto/1.2727)));
		
		FilaColumna.setEnabled(false);
		FilaColumna.setOpaque(false);
		FilaColumna.setDisabledTextColor(Color.black);
		FilaColumna.setBorder(null);
		
		
		
		//Agregar al JFrame
		add(lbPrograma);
		add(lbResultado);
		add(lbParser);
                add(lbEscanear);
		add(lbLogo); 
                
		add(Prog);
		add(Calcular);
		add(Res);
		add(RPar);
		
		add(lbTotal);	
		add(FilaColumna);
		
	
		Resultado.setEnabled(false);
		Resultado.setOpaque(true);
		Resultado.setDisabledTextColor(Color.black);
		
		ResParser.setEnabled(false);
		ResParser.setOpaque(true);
		ResParser.setDisabledTextColor(Color.black);
		
	
		lbPrograma.setFont(new Font("Verdana", Font.BOLD, 16));
		lbResultado.setFont(new Font("Verdana", Font.BOLD, 16));
		lbParser.setFont(new Font("Verdana", Font.BOLD, 16));
                lbEscanear.setFont(new Font("Verdana", Font.BOLD, 16));
		
		Calcular.setContentAreaFilled(false);
		
		Calcular.setIcon((new ImageIcon(((new ImageIcon("analyze.png")).getImage()).getScaledInstance
				(Calcular.getWidth(), Calcular.getHeight(), java.awt.Image.SCALE_AREA_AVERAGING))));
		
                lbLogo.setIcon(new ImageIcon("LOGO_TEC_PNG_OK.png"));
        
		
		cerrar();
		//Poner visible
		
		setVisible(true);
		
	}
	
	public void actionPerformed(ActionEvent evento){
		
		if(evento.getSource() instanceof JMenuItem){
			if (evento.getSource() == elementoNuevo ){
				Nuevo();
				return;
			}
			
			if (evento.getSource() == elementoAbrir ){
				Programa.setText(abrirArchivo());
				return;
			}
			
			if (evento.getSource() == elementoGuardar ){
				guardarArchivo();
				return;
			}

			if (evento.getSource() == elementoGuardarComo ){
				guardarComoArchivo();
				return;
			}
			
			if (evento.getSource() == elementoSalir && !guarda && !Programa.getText().equals(""))
	    	{
	    		int ax = JOptionPane.showConfirmDialog(null, "¿Quieres guardar?");
	            if(ax == JOptionPane.YES_OPTION)
	                guardarArchivo();
	            else if(ax == JOptionPane.NO_OPTION)
	            	
	            	System.exit( 0 ); // Salir del programa
	    	}
			else
				System.exit( 0 ); //Sale

		}
		
		//Boton de analisis
		if(evento.getSource() instanceof JButton){
			if (evento.getSource() == Calcular){
				Resultado.setText("");
				
				if(Programa.getText().length()<1){
					
					ResParser.setText("El programa es correcto ");
					ResParser.setDisabledTextColor(Color.BLUE);
					return;
				}
				
				//Indica que se escaneo
				Escaneado = true;
				
				TokenTipo = "Token\tTipo\n";
				
				ObjAnalizador =new Analizador(Programa.getText());
				Resultado.setText(TokenTipo);
				
				VectorTokens=ObjAnalizador.getVectorTokens();
				lbTotal.setText("# de Tokens = "+VectorTokens.size());
			
				//Se va viendo el parser
				//No deja usar parser si no se a escaneado
				if(!Escaneado){
					JOptionPane.showMessageDialog(null,
			    	         "El programa no ha sido Escaneado, Escanee para usar el Parser",
			    	             "Información",JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				//No se puede usar parser si se escaneo mal
				if(!ObjAnalizador.isCorrect()){
					/*JOptionPane.showMessageDialog(null,
			    	         "El programa debe tener solo tokens validos para usar el Parser",
			    	             "Información",JOptionPane.INFORMATION_MESSAGE);*/
					ResParser.setDisabledTextColor(Color.RED);
					ResParser.setText("Programa Incorrecto");
					return;
				}
				ObjParser = new ParserClass(VectorTokens);
				
				ResParser.setText("");
				
				if(!ObjParser.isCorrect()){
					ResParser.setDisabledTextColor(Color.RED);
					ResParser.setText("Syntax Error\n");
				}else
					ResParser.setDisabledTextColor(Color.BLUE);
				
				ResParser.setText(ResParser.getText()+ObjParser.Resultado());
				
				
			}
			
		}
		
	}
	
	//Nuevo
    public void Nuevo () {

    	if (! guarda && !Programa.getText().equals(""))
    	{
    		int ax = JOptionPane.showConfirmDialog(null, "¿Desea guardar el archivo?");
            if(ax == JOptionPane.YES_OPTION){
                guardarArchivo();
                Programa.setText("");
                Resultado.setText("");
                NombreDoc = null;
            }
            else if(ax == JOptionPane.NO_OPTION){
            	Programa.setText("");
            	Resultado.setText("");
            	NombreDoc = null;
            }
    		guarda=false;
    	}
    }
    
    
    //Abrir
    private String abrirArchivo() {
    	if (! guarda && !Programa.getText().equals(""))
    	{
    		int ax = JOptionPane.showConfirmDialog(null, "¿Desea guardar el archivo?");
            if(ax == JOptionPane.YES_OPTION)
            guardarArchivo();
            Programa.setText("");
            Resultado.setText("");
    	}
    	  String aux="", texto="";
    	  try
    	  {
    	   //Llamamos el metodo que permite cargar la ventana
    	   JFileChooser file=new JFileChooser();
    	   file.showOpenDialog(this);
    	   //Abrimos el archivo seleccionado
    	   File abre=file.getSelectedFile();
    	   
    	   NombreDoc=abre.getParent()+"/"+abre.getName(); //Aquí es donde toma la dirección del texto
    	   
    	   //Recorremos el archivo, lo leemos para plasmarlo en el area de texto
    	   if(abre!=null)
    	   {     
    	      FileReader archivos=new FileReader(abre);
    	      BufferedReader lee=new BufferedReader(archivos);
    	      while((aux=lee.readLine())!=null)
    	      {
    	         texto+= aux+ "\n";
    	      }
    	         lee.close();
    	    }    
    	   }
    	   catch(IOException ex)
    	   {
    	     JOptionPane.showMessageDialog(null,ex+"" +
    	           "\nNo se ha encontrado el archivo",
    	                 "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
    	    }
    	  return texto;//El texto se almacena en el JTextArea
    	}

    //Guardar
	private void guardarArchivo() {

		//NombreDoc es una variable global de tipo String que contiene la dirección del archivo actual
		if(NombreDoc==null)//Para cuando le des guardar y no exista el archivo aún
			guardarComoArchivo();
		else{
			File doc=new File(NombreDoc);
			doc.delete();//Elimino el archivo actual
			File nuevoDoc=new File(NombreDoc);//Creo el nuevo archivo
			String texto = Programa.getText();//Tomo el texto que está en el JTextArea
			//System.out.println(source);

			try {
				FileWriter f2 = new FileWriter(nuevoDoc, false);//Hago que pueda escribir en el neuvo doc
				f2.write(texto);//Le escribo dicho texto en el nuevo documento
				f2.close();
				JOptionPane.showMessageDialog(null,
		    	         "El archivo se ha guardado Exitosamente",
		    	             "Información",JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}  
    }
	
	//Guardar como
		private void guardarComoArchivo() {

	    	 try{
	    	  
	    	  JFileChooser file=new JFileChooser();
	    	  file.showSaveDialog(this);
	    	  
	    	  File guarda =file.getSelectedFile();
	    	  
	    	  if(guarda !=null){
	    	   //Guardamos el archivo y le damo formato
	    	    
	    	    FileWriter  save=new FileWriter(guarda);
	    	  
	    	    save.write(Programa.getText());
	    	    save.close();
	    	    JOptionPane.showMessageDialog(null,
	    	         "El archivo se ha guardado.",
	    	             "Información",JOptionPane.INFORMATION_MESSAGE);
	    	    }
	    	  }
	    	  catch(IOException ex){
	    	   JOptionPane.showMessageDialog(null,
	    	        "Su archivo no se ha guardado.",
	    	           "Advertencia",JOptionPane.WARNING_MESSAGE);
	    	  }
	    }
	
	//------------------------------------------------------------------->Validar el CIERRE con X
	public void cerrar()
	{
		
		try{
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			
			addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					confirmarSalida();
				}
			});
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void confirmarSalida(){
		int valor=JOptionPane.showConfirmDialog(this, "¿Ya te vas, no quieres guardar?", "Salir", JOptionPane.YES_NO_OPTION);
		
		if  (valor==JOptionPane.YES_OPTION){
			guardarArchivo();
			System.exit(0);
		}
		else  if (valor==JOptionPane.NO_OPTION)
				System.exit(0); 
	}
	
	//Indicar fila y columna
	public void caretUpdate(CaretEvent e) {
		  JTextArea editArea = (JTextArea)e.getSource();
		 
		  int linea = 1;
		  int columna = 1;
		 
		  try {
		    int caretpos = editArea.getCaretPosition();
		    linea= editArea.getLineOfOffset(caretpos);
		    columna = caretpos - editArea.getLineStartOffset(linea);
		    
		    
		    // Ya que las líneas las cuenta desde el 0
		    linea += 1;
		  } catch(Exception ex) { }
		 
		  // Actualizamos el estado
		  actualizarEstado(linea, columna);
	}
	
	private void actualizarEstado(int linea, int columna) {
		FilaColumna.setText("Fila: " + linea + "\t Columna: " + columna);
	}
	
	//Paser no inicia antes de escaner si hay cambios
	public void insertUpdate(DocumentEvent e) {
		Escaneado = false;
		lbTotal.setText("# de Tokens = ");
	
    }
    public void removeUpdate(DocumentEvent e) {
    	Escaneado = false;
    	lbTotal.setText("Tokens = ");
 
    }
    public void changedUpdate(DocumentEvent e) {
        //Plain text components do not fire these events
    	Escaneado = false;
    	lbTotal.setText("Tokens = ");
 
    }
	
	public static void main(String[] args) {
		new Interfaz();
	}

}
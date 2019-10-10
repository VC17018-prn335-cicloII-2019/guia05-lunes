/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ues.occ.edu.sv.ingenieria.prn335.guia05.boundary;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;
import ues.occ.edu.sv.ingenieria.prn335.cineData.entity.Clasificacion;
import ues.occ.edu.sv.ingenieria.prn335.cineData.entity.Director;
import ues.occ.edu.sv.ingenieria.prn335.cineData.entity.Genero;
import ues.occ.edu.sv.ingenieria.prn335.cineData.entity.Pelicula;
import ues.occ.edu.sv.ingenieria.prn335.guia05.control.AbstractFacade;
import ues.occ.edu.sv.ingenieria.prn335.guia05.control.ClasificacionFacade;
import ues.occ.edu.sv.ingenieria.prn335.guia05.control.DirectorFacade;
import ues.occ.edu.sv.ingenieria.prn335.guia05.control.GeneroFacade;
import ues.occ.edu.sv.ingenieria.prn335.guia05.control.PeliculaFacade;

/**
 *
 * @author mario
 */
@Named(value = "peliculaBean")
@ViewScoped
public class PeliculaBean extends BackingBean<Pelicula> implements Serializable {

    @Inject
    private PeliculaFacade pelicula;
    @Inject
    private DirectorFacade directorfacade;
    private List<Director> directorList;
    @Inject
    private ClasificacionFacade clasificacionfacade;
    private List<Clasificacion> clasificacionList;
    @Inject
    private GeneroFacade generofacade;
    private List<Genero> genero;

    @PostConstruct
    @Override
    public void iniciar() {
        super.iniciar(); //To change body of generated methods, choose Tools | Templates.
        iniciarRelaciones();
    }

    public void iniciarRelaciones() {
        if (clasificacionfacade != null && directorfacade != null && generofacade != null) {
            directorList = directorfacade.findAll();
            clasificacionList = clasificacionfacade.findAll();
            genero = generofacade.findAll();
        } else {
            directorList = Collections.EMPTY_LIST;
            clasificacionList = Collections.EMPTY_LIST;
            genero = Collections.EMPTY_LIST;
        }
    }

    @Override
    public Object clavePorDatos(Pelicula object) {
        if (object != null) {
            return object.getIdPelicula();
        }
        return null;
    }

    @Override
    public Pelicula datosPorClave(String rowKey) {
        if (rowKey != null && !rowKey.isEmpty()) {
            try {
                Integer search = new Integer(rowKey);
                for (Pelicula tu : this.List) {
                    if (tu.getIdPelicula().compareTo(search) == 0) {
                        return tu;
                    }
                }
            } catch (NumberFormatException e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public Pelicula getRegistro() {
        if (this.registro == null) {
            this.registro = new Pelicula();
        }
        return super.getRegistro(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LazyDataModel<Pelicula> getModelo() {
        return super.getModelo(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected AbstractFacade<Pelicula> getFacade() {
        return pelicula;
    }

    public List<Director> getDirectorList() {
        return directorList;
    }

    public List<Clasificacion> getClasificacionList() {
        return clasificacionList;
    }

    public List<Genero> getAllGenero() {
        return genero;
    }

    public void setAllGenero(List<Genero> allGenero) {
        this.genero = allGenero;
    }

    @Override
    public void btnEditarHandler(ActionEvent ae) {
        List<Genero> generoRegistro = pelicula.find(registro.getIdPelicula()).getGeneroList();
        if (registro != null) {
            System.out.println("Entro satisfactoriamente");
            if (generoRegistro.size() > registro.getGeneroList().size()) {
                for (int i = 0; i < generoRegistro.size(); i++) {
                    if (registro.getGeneroList().contains(generoRegistro.get(i)) == false) {
                        generoRegistro.get(i).getPeliculaList().remove(registro);
                        generofacade.edit(generoRegistro.get(i));
                        System.out.println("Se borró genero satisfactoriamente");
                    }

                }
            } else {
                for (int i = 0; i < registro.getGeneroList().size(); i++) {
                    if (generoRegistro.contains(registro.getGeneroList().get(i)) == false) {
                        registro.getGeneroList().get(i).getPeliculaList().add(registro);
                        generofacade.edit(registro.getGeneroList().get(i));
                        System.out.println("Se agregó genero satisfactoriamente");
                    }
                }
            }
        }
        getFacade().edit(registro);
        iniciar();
    }

    @Override
    public void btnAgregarHandler(ActionEvent ae) {
        if (registro != null || registro.getGeneroList() != null) {
            List<Genero> generoRegistro = registro.getGeneroList();
            registro.setGeneroList(Collections.EMPTY_LIST);
            pelicula.create(registro);
            registro.setGeneroList(generoRegistro);
            this.btnEditarHandler(ae);
        }
    }

}

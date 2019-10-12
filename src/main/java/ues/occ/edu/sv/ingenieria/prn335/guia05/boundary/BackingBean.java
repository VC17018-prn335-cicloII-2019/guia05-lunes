/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ues.occ.edu.sv.ingenieria.prn335.guia05.boundary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.ActionEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ues.occ.edu.sv.ingenieria.prn335.guia05.control.AbstractFacade;

/**
 *
 * @author mario
 * @param <T>
 */
public abstract class BackingBean<T> {

    public abstract Object clavePorDatos(T object);

    public abstract T datosPorClave(String rowKey);

    protected abstract AbstractFacade<T> getFacade();
    List<T> List = new ArrayList<>();
    protected T registro;
    protected LazyDataModel<T> modelo;

    public void iniciar() {
        Modelo();
        if (getFacade().findAll() != null) {
            List = getFacade().findAll();
        } else {
            List = Collections.EMPTY_LIST;
        }
        this.modelo.setRowIndex(-1);
        registro = null;
    }
/** 
 * Los siguientes metodosson los botones CRUD, cancelar para que sean dinamicos 
 * con el composite.
 * @param event 
 */
    public void onRowSelect(SelectEvent event) {
        registro = (T) event.getObject();
    }

    public void onRowDeselect(UnselectEvent event) {
        this.modelo.setRowIndex(-1);
    }

    public void btnCancelarHandler(ActionEvent ae) {
        iniciar();
    }

    public void btnAgregarHandler(ActionEvent ae) {
        if (registro != null) {
            getFacade().create(registro);
            iniciar();
        }
    }

    public void btnEditarHandler(ActionEvent ae) {
        if (registro != null) {
            getFacade().edit(registro);
            iniciar();
        }
    }

    public void btnEliminarHandler(ActionEvent ae) {
        if (getFacade() != null && registro != null) {
            try {
                getFacade().remove(registro);
                iniciar();
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    /**
     * Se crea el metodo lazyData model para que sea dinamico para todas 
     * las entidades.
     * @return 
     */
    public LazyDataModel<T> Modelo() {
        try {
            modelo = new LazyDataModel<T>() {
                @Override
                public Object getRowKey(T object) {
                    return clavePorDatos(object);
                }

                @Override
                public T getRowData(String rowKey) {
                    return datosPorClave(rowKey);
                }

                @Override
                public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    List<T> salida = new ArrayList<>();
                    try {
                        if (getFacade() != null) {
                            this.setRowCount(getFacade().count());
                            salida = getFacade().findRange(first, pageSize);
                        }

                    } catch (Exception e) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
                    }
                    return salida;
                }
            };
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public T getRegistro() {
        return registro;
    }

    public void setRegistro(T registro) {
        this.registro = registro;
    }

    public LazyDataModel<T> getModelo() {
        return modelo;
    }

    public void setModelo(LazyDataModel<T> modelo) {
        this.modelo = modelo;
    }

    public List<T> getList() {
        return List;
    }

}

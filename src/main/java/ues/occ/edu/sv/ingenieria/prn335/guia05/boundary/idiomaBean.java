/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ues.occ.edu.sv.ingenieria.prn335.guia05.boundary;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author mario
 */
@Named(value = "idiomaBean")
@ViewScoped
public class idiomaBean implements Serializable {

    private String codigoIdioma = "es";

    private static Map<String, Object> paises;

    static {
        paises = new LinkedHashMap<>();
        paises.put("Español", Locale.forLanguageTag("es"));
        paises.put("Ingles", Locale.US);
    }

    public Map<String, Object> getPaises() {
        return paises;
    }

    public String getCodigoIdioma() {
        return codigoIdioma;
    }

    public void setCodigoIdioma(String codigoIdioma) {
        this.codigoIdioma = codigoIdioma;
    }

    public void cambioDeIdiomaHandler(ValueChangeEvent evt) {
        if (evt.getNewValue() != null) {
            try {
                String NuevoCodigo = evt.getNewValue().toString();
                for (Map.Entry<String, Object> entry : paises.entrySet()) {
                    Locale value = (Locale) entry.getValue();
                    if (value.toString().compareTo(NuevoCodigo) == 0) {
                        FacesContext.getCurrentInstance().getViewRoot().setLocale(value);
                        return;
                    }
                }
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

}

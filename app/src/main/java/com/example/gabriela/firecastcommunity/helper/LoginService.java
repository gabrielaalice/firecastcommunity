package com.example.gabriela.firecastcommunity.helper;

import com.example.gabriela.firecastcommunity.domain.LoginValidation;

/**
 * Created by Gabriela on 10/16/17.
 */

public class LoginService {

    public void validarCamposLogin(LoginValidation validation) {
        boolean resultado = true;
        if (validation.getLogin() == null || "".equals(validation.getLogin())) {
            validation.getEdtLogin().setError("Campo obrigatório!");
            resultado = false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(validation.getLogin()).matches()) {
            validation.getEdtLogin().setError("E-mail inválido");
            resultado = false;
        }

        if (validation.getSenha() == null || "".equals(validation.getSenha())) {
            validation.getEdtSenha().setError("Campo obrigatório!");
            resultado = false;
        }

        if (resultado) {
            new AsyncUser(validation).execute(Constantes.URL_WS_LOGIN);
        }
    }

    public void deslogar() {

    }

}

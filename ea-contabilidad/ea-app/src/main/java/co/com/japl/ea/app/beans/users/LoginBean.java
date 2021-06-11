package co.com.japl.ea.app.beans.users;

import static org.pyt.common.constants.languages.Login.CONST_MSN_PASSWORD_ERROR;
import static org.pyt.common.constants.languages.Login.CONST_MSN_USER_ERROR;
import static org.pyt.common.constants.languages.Login.CONST_TITLE_LOGIN;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.pyt.common.annotations.Inject;
import org.pyt.common.constants.LanguageConstant;
import org.pyt.common.constants.StylesPrincipalConstant;

import co.com.arquitectura.annotation.proccessor.FXMLFile;
import co.com.japl.ea.app.load.Template;
import co.com.japl.ea.beans.abstracts.ABean;
import co.com.japl.ea.common.button.apifluid.ButtonsImpl;
import co.com.japl.ea.common.validates.ValidFields;
import co.com.japl.ea.dto.interfaces.IUsersSvc;
import co.com.japl.ea.dto.system.UsuarioDTO;
import co.com.japl.ea.exceptions.LoadAppFxmlException;
import co.com.japl.ea.utls.LoadAppFxml;
import co.com.japl.ea.utls.LoginUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Se encarga de procesar la pantalla de creacion y actualizacion de una
 * actividad ica
 * 
 * @author Alejandro Parra
 * @since 2018-05-22
 */
@FXMLFile(path = "view/users", file = "login.fxml")
public class LoginBean extends ABean<UsuarioDTO> {

	@Inject
	private IUsersSvc usersSvc;
	@FXML
	private TextField user;
	@FXML
	private PasswordField password;
	@FXML
	private CheckBox remember;
	@FXML
	private Label message;
	@FXML
	private Label title;
	@FXML
	private HBox buttons;
	private Stage stage;
	private Boolean login = false;
	private BooleanProperty connect;
	private BooleanProperty clear;

	@FXML
	public void initialize() {
		connect = new SimpleBooleanProperty(false);
		clear = new SimpleBooleanProperty(false);
		registro = new UsuarioDTO();
		configFields();
		title.setText(i18n().valueBundle(CONST_TITLE_LOGIN).get());
		verifyLoginRemember();
	}

	private void configButtons() {
		ButtonsImpl.Stream(HBox.class).setLayout(buttons).setName("fxml.form.button.connect").action(this::connect)
				.icon(Glyph.SIGN_IN).setCommand("G").isVisible(connect).setName("fxml.form.button.clear.all")
				.action(this::clearAll).icon(Glyph.ERASER).setCommand("L").isVisible(clear)
				.setName("fxml.form.button.cancel").action(this::cancel).setCommand("C").build();
	}

	private void configFields() {
		user.setPromptText(i18n("inputtext.promp.usuario.user.name"));
		password.setPromptText(i18n("inputtext.promp.usuario.password"));
		user.textProperty().addListener((observable, oldValue, newValue) -> {
			if (StringUtils.isNotBlank(password.getText()) || StringUtils.isNotBlank(newValue)) {
				clear.set(true);
			} else {
				clear.set(false);
			}
			if (StringUtils.isNotBlank(password.getText()) && StringUtils.isNotBlank(newValue)) {
				connect.set(true);
			} else {
				connect.set(false);
			}
		});
		password.textProperty().addListener((observable, oldValue, newValue) -> {
			if (StringUtils.isNotBlank(user.getText()) || StringUtils.isNotBlank(newValue)) {
				clear.set(true);
			} else {
				clear.set(false);
			}
			if (StringUtils.isNotBlank(user.getText()) && StringUtils.isNotBlank(newValue)) {
				connect.set(true);
			} else {
				connect.set(false);
			}
		});
	}

	private void verifyLoginRemember() {
		try {
			var loadLogin = LoginUtil.loadLogin();
			loadLogin.ifPresent(value -> {
				remember.setSelected(true);
				LoginUtil.validUsuarioRememberFails(value);
				findLogin(value);
			});
		} catch (IOException e) {
			error(e);
		} catch (ClassNotFoundException e) {
			error(e);
		} catch (RuntimeException e) {
			error(e);
		} catch (Exception e) {
			error(e);
		}

	}

	private void findLogin(UsuarioDTO value) {
		try {
			UsuarioDTO found;
			found = usersSvc.login(value, remoteAddr(), remember.isSelected());
			if (found != null) {
				LoginUtil.compareUsuariosRememberAndFound(found, value);
				LoginUtil.writeRemember(found);
				setUsuario(found);
				this.login = true;
			}
		} catch (UnknownHostException e) {
			error(e);
		} catch (Exception e) {
			error(e);
		}

	}

	public void load(Stage stage) {
		this.stage = stage;
		configButtons();
		if (login) {
			try {
				loadTemplate();
			} catch (LoadAppFxmlException e) {
				error(e);
			}
		}
	}

	private final boolean valid() {
		var valid = true;
		valid &= ValidFields.valid(user, true, 3, 20, i18n().valueBundle(CONST_MSN_USER_ERROR));
		valid &= ValidFields.valid(password, true, 3, 20, i18n().valueBundle(CONST_MSN_PASSWORD_ERROR));
		return valid;
	}

	private void loadTemplate() throws LoadAppFxmlException {
		this.destroy();
		if (Optional.ofNullable(stage).isPresent()) {
			stage.hide();
		}
		LoadAppFxml.loadFxml(new Stage(), Template.class);
	}

	public void connect() {
		try {
			if (valid()) {
				registro.setNombre(user.getText());
				registro.setPassword(LoginUtil.encodePassword(user.getText(), password.getText()));
				var user = usersSvc.login(registro, remoteAddr(), remember.isSelected());
				user.setPassword(null);
				this.setUsuario(user);
				if (remember.isSelected()) {
					LoginUtil.writeRemember(getUsuario());
				}
				loadTemplate();
			}
		} catch (NullPointerException e) {
			registro.setPassword(null);
			password.setText(null);
			message.setText(i18n().valueBundle(LanguageConstant.CONST_ERR_NULL_POINTER_EXCEPTION_LOGIN).get());
			message.getStyleClass().add(StylesPrincipalConstant.CONST_MESSAGE_ERROR);
		} catch (RuntimeException e) {
			registro.setPassword(null);
			password.setText(null);
			alerta(e.getMessage());
			message.setText(e.getMessage());
			message.getStyleClass().add(StylesPrincipalConstant.CONST_MESSAGE_WARN);
		} catch (Exception e) {
			error(e);
			registro.setPassword(null);
			password.setText(null);
			message.setText(e.getMessage());
			message.getStyleClass().add(StylesPrincipalConstant.CONST_MESSAGE_ERROR);
		}
	}

	public void clearAll() {
		user.setText(null);
		user.setTooltip(null);
		password.setText(null);
		password.setTooltip(null);
		registro = new UsuarioDTO();
		remember.setSelected(false);
		message.setText(null);
	}

	public void cancel() {
		try {
			System.exit(0);
		} catch (Exception e) {
			error(e);
		}
	}

	private String remoteAddr() throws UnknownHostException {
		// return getContext().getRemoteAddr();
		return InetAddress.getLocalHost().getHostAddress();
	}

	@Override
	protected void visibleButtons() {
		// TODO Auto-generated method stub

	}

}

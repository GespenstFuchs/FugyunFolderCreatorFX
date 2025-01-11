package fugyunfoldercreatorfx;

import java.io.File;
import java.util.List;
import java.util.Optional;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * カスタムメッセージボックス
 */
public class CustomMessageBox
{
	/**
	 * デフォルトボタン
	 */
	public Button defaultButton;

	/**
	 * メッセージボックス表示処理
	 * @param ownerStage 親ステージ（nullの場合、最小化ボタン・最大化ボタンが表示され、タスクバーにも別ウィンドウとして表示される。）
	 * @param title　タイトル
	 * @param message メッセージ
	 * @param alertType アラートタイプ
	 * @param buttonTextList ボタンテキストリスト
	 * @param defaultButtonIndex デフォルトボタン位置（０ＢＡＳＥ）
	 * @return　ボタンに応じた位置
	 */
	public Optional<ButtonType> showMessageBox(
			Stage ownerStage,
			String title,
			String message,
			AlertType alertType,
			List<String> buttonTextList,
			int defaultButtonIndex)
	{
		// メッセージボックスを生成する。
		Stage dialogStage = new Stage();
		dialogStage.initOwner(ownerStage);
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.setResizable(false);
		dialogStage.addEventHandler(WindowEvent.WINDOW_SHOWN, event ->
		{
			// デフォルトボタンを設定する。
			defaultButton.requestFocus();
		});

		// タイトルを設定する。
		dialogStage.setTitle(title);

		// アイコン（タイトルバー）を設定する。
		Image img = new Image(new File("./resources/Icon.png").toURI().toString());
		dialogStage.getIcons().add(img);

		VBox vbox = new VBox(10);
		vbox.setPadding(new javafx.geometry.Insets(10, 20, 20, 20));

		// アイコンを設定する。
		ImageView iconView = new ImageView();
		iconView.setFitWidth(50);
		iconView.setFitHeight(50);

		String iconPath;
		if (alertType == AlertType.INFORMATION)
		{
			iconPath = "/Infomation.png";
		}
		else if (alertType == AlertType.ERROR)
		{
			iconPath = "/Error.png";
		}
		else if (alertType == AlertType.WARNING)
		{
			iconPath = "/Warning.png";
		}
		else
		{
			iconPath = "/Question.png";
		}

		Image image = new Image(CustomMessageBox.class.getResource(iconPath).toExternalForm());
		iconView.setImage(image);

		// メッセージを設定する。
		Label messageLabel = new Label(message);
		messageLabel.setFont(Font.font("Yu Gothic UI", 14));
		HBox messageBox = new HBox(10, iconView, messageLabel);
		messageBox.setAlignment(Pos.CENTER_LEFT);

		// ボタンボックスを生成する。
		HBox buttonBox = new HBox(10);
		buttonBox.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));
		buttonBox.setAlignment(Pos.CENTER);

		// ボタンを設定する。
		ButtonType[] buttonTypes = new ButtonType[buttonTextList.size()];
		for (int index = 0; index < buttonTextList.size(); index++)
		{
			// ボタンを生成する。
			String text = buttonTextList.get(index);
			ButtonType buttonType = new ButtonType(text, ButtonData.OK_DONE);
			buttonTypes[index] = buttonType;
			Button button = new Button(text);
			button.setPrefWidth(100);
			button.setFont(Font.font("Yu Gothic UI", 14));
			button.setOnAction(e ->
			{
				dialogStage.setUserData(buttonType);
				dialogStage.close();
			});

			// デフォルトボタンを設定する。
			if (index == defaultButtonIndex)
			{
				defaultButton = button;

				//　デフォルト値を設定する。
				dialogStage.setUserData(buttonType);
			}
			buttonBox.getChildren().add(button);
		}

		// メッセージ・ボタンを設定する。
		vbox.getChildren().addAll(messageBox, buttonBox);
		Scene scene = new Scene(vbox);
		dialogStage.setScene(scene);

		// メッセージボックスを表示し、戻り値を返却する。
		dialogStage.showAndWait();
		return Optional.ofNullable((ButtonType) dialogStage.getUserData());
	}
}

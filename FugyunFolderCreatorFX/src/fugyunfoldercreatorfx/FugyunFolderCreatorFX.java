package fugyunfoldercreatorfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

/**
 * ふぎゅんフォルダ作成
 */
public class FugyunFolderCreatorFX extends Application
{
	/**
	 * 開始メソッド
	 * @param primaryStage ステージ
	 */
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			// FXMLファイルを読み込む。
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainFrame.fxml"));
			Parent root = loader.load();

			// コントローラーに、ステージを渡す。
			MainFrameController controller = loader.getController();
			controller.setPrimaryStage(primaryStage);

			// ドラッグオーバーイベントを設定する。
			root.setOnDragOver(event ->
			{
				if (event.getGestureSource() != root && event.getDragboard().hasFiles())
				{
					event.acceptTransferModes(TransferMode.COPY);
				}
				event.consume();
			});

			// ドラッグ後イベントを設定する。
			root.setOnDragDropped(event -> controller.handleFileDrop(event));

			// シーンを設定する。
			Scene scene = new Scene(root);

			// キー押下処理設定処理を呼び出す。
			controller.setOnKeyPressed(scene);

			// アイコンをリソースから読み込み、設定する。
			Image icon = new Image(getClass().getResourceAsStream("/Icon.png"));
			primaryStage.getIcons().add(icon);

			primaryStage.setTitle("ふぎゅんフォルダ作成");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * メイン処理
	 * @param args パラメータ配列
	 */
	public static void main(String[] args)
	{
		try
		{
			launch(args);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

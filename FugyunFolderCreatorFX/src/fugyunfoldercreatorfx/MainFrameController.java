package fugyunfoldercreatorfx;

import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * メインフレームコントローラー
 */
public class MainFrameController
{
	/**
	 * 優先ステージ
	 */
	private Stage primaryStage;

	/**
	 * ふぎゅんユーティリティ
	 */
	private FugyunUtilityFX utility;

	/**
	 * フォルダ作成ボタン
	 */
	@FXML
	private Button folderCreateButton;

	/**
	 * フォルダ削除ボタン
	 */
	@FXML
	private Button folderDeleteButton;

	/**
	 * フォルダ全削除ボタン
	 */
	@FXML
	private Button folderAllDeleteButton;

	/**
	 * 入力したパスをクリアボタン
	 */
	@FXML
	private Button pathClearButton;

	/**
	 * パステキストエリア
	 */
	@FXML
	private TextArea pathTextArea;

	/**
	 * パステキストエリアコンテキストメニュー
	 */
	@FXML
	private ContextMenu pathTextAreaContextMenu;

	/**
	 * 元に戻すメニュー
	 */
	@FXML
	private MenuItem undoMenuItem;

	/**
	 * やり直しメニュー
	 */
	@FXML
	private MenuItem redoMenuItem;

	/**
	 * 優先ステージ設定処理
	 * @param stage ステージ
	 */
	public void setPrimaryStage(Stage stage)
	{
		primaryStage = stage;
		utility = new FugyunUtilityFX(primaryStage);
	}

	/**
	 * キー押下処理設定処理
	 * @param scene シーン
	 */
	public void setOnKeyPressed(Scene scene)
	{
		// ショートカットを設定する。
		scene.setOnKeyPressed(event ->
		{
			if (event.getCode() == KeyCode.F1)
			{
				handleFolderCreate();
			}
			else if (event.getCode() == KeyCode.F2)
			{
				handleFolderDelete();
			}
			else if (event.getCode() == KeyCode.F3)
			{
				handleFolderAllDelete();
			}
			else if (event.getCode() == KeyCode.F4)
			{
				handlePathClear(null);
			}
		});
	}

	/**
	 * ドロップハンドル
	 * @param event イベント
	 */
	public void handleFileDrop(DragEvent event)
	{
		try
		{
			Dragboard dragboard = event.getDragboard();
			boolean success = false;

			if (dragboard.hasFiles())
			{
				List<File> files = dragboard.getFiles();
				StringBuilder folderPathStr = new StringBuilder();

				// 全ファイルに処理を行う。
				for (File file : files)
				{
					// ファイルを判定する。
					if (file.isDirectory())
					{
						// フォルダの場合
						folderPathStr.append(file.getAbsolutePath()).append("\n");
					}
				}

				// パステキストエリアに、パスを追加する。
				pathTextArea.appendText(folderPathStr.toString());
				success = true;
			}

			event.setDropCompleted(success);
			event.consume();
		}
		catch (Exception e)
		{
			new CustomMessageBox().showMessageBox(
					primaryStage,
					Const.ERROR,
					e.getMessage(),
					AlertType.ERROR,
					Const.BUTTON_TEXT_LIST_OK_ONLY,
					0);
		}
	}

	/**
	 * 初期化処理
	 */
	@FXML
	private void initialize()
	{
		// パステキストエリアにフォーカスを設定する。
		Platform.runLater(() -> pathTextArea.requestFocus());

		pathTextArea.addEventFilter(KeyEvent.KEY_PRESSED, event ->
		{
			// キーコードを判定する。
			if (event.getCode() == KeyCode.TAB)
			{
				// 入力をキャンセルする。
				event.consume();

				// Shiftキー押下を判定する。
				if (event.isShiftDown())
				{
					// 押下されている場合、入力したパスをクリアボタンをフォーカスを設定する。
					pathClearButton.requestFocus();
				}
				else
				{
					// 押下されている場合、フォルダ作成ボタンをフォーカスを設定する。
					folderCreateButton.requestFocus();
				}
			}
		});

		pathTextAreaContextMenu.setOnShowing(event -> setDisableMenuItems());
	}

	/**
	 * フォルダ作成ハンドル
	 */
	@FXML
	public void handleFolderCreate()
	{
		utility.FolderCreateTran(pathTextArea.getText());
		pathTextArea.requestFocus();
	}

	/**
	 * フォルダ削除ハンドル
	 */
	@FXML
	public void handleFolderDelete()
	{
		utility.FolderDeleteTran(false, pathTextArea.getText());
		pathTextArea.requestFocus();
	}

	/**
	 * フォルダ全削除ハンドル
	 */
	@FXML
	public void handleFolderAllDelete()
	{
		utility.FolderDeleteTran(true, pathTextArea.getText());
		pathTextArea.requestFocus();
	}

	/**
	 * パスをクリアするハンドル
	 * @param event イベント
	 */
	@FXML
	public void handlePathClear(ActionEvent event)
	{
		pathTextArea.clear();
		pathTextArea.requestFocus();
	}

	/**
	 * 元に戻すハンドル
	 */
	@FXML
	private void handleUndo()
	{
		pathTextArea.undo();
	}

	/**
	 * やり直しハンドル
	 */
	@FXML
	private void handleRedo()
	{
		pathTextArea.redo();
	}

	/**
	 * メニュー項目活性状態設定処理
	 */
	private void setDisableMenuItems()
	{
		undoMenuItem.setDisable(!pathTextArea.isUndoable());
		redoMenuItem.setDisable(!pathTextArea.isRedoable());
	}

	/**
	 * 切り取りハンドル
	 */
	@FXML
	private void handleCut()
	{
		pathTextArea.cut();
	}

	/**
	 * コピーハンドル
	 */
	@FXML
	private void handleCopy()
	{
		pathTextArea.copy();
	}

	/**
	 * 貼り付け井ハンドル
	 */
	@FXML
	private void handlePaste()
	{
		pathTextArea.paste();
	}

	/**
	 * 削除ハンドル
	 */
	@FXML
	private void handleDelete()
	{
		String text = pathTextArea.getText();

		if (text.isBlank())
		{
			return;
		}

		int selectionStart = pathTextArea.getSelection().getStart();
		int selectionEnd = pathTextArea.getSelection().getEnd();

		// 選択範囲を判定する。
		if (selectionStart == selectionEnd)
		{
			// 存在しない場合
			int newLineLength = System.lineSeparator().length();
			int removeLength = 1;

			// 選択開始位置＋改行コード数・文字数を判定する
			if (selectionStart + newLineLength <= text.length())
			{
				if (text.substring(selectionStart, selectionStart + newLineLength).equals(System.lineSeparator()))
				{
					// 改行コードを削除する
					removeLength = newLineLength;
				}
			}

			pathTextArea.setText(text.substring(0, selectionStart) + text.substring(selectionStart + removeLength));
			pathTextArea.positionCaret(selectionStart);
		}
		else
		{
			// 存在する場合
			pathTextArea.replaceText(selectionStart, selectionEnd, "");
		}
	}

	/**
	 * 行選択ハンドル
	 */
	@FXML
	private void handleSelectLine()
	{
		String text = pathTextArea.getText();

		if (text.isBlank())
		{
			return;
		}

		// 選択開始行位置・選択終了行位置を取得・保持する。
		int startLineIndex = pathTextArea.getText(0, pathTextArea.getSelection().getStart()).split("\n", -1).length - 1;
		int endLineIndex = pathTextArea.getText(0, pathTextArea.getSelection().getEnd()).split("\n", -1).length - 1;

		// 選択開始位置・選択終了位置を取得・保持する。
		int lineStartOffset = getLineStartOffset(text, startLineIndex);
		int lineEndOffset = getLineEndOffset(text, endLineIndex);

		// 選択範囲を設定する。
		pathTextArea.selectRange(lineStartOffset, lineEndOffset);

		pathTextArea.requestFocus();
	}

	/**
	 * 行開始位置取得処理
	 * @param text テキスト
	 * @param lineIndex 行位置
	 * @return　行開始位置
	 */
	private int getLineStartOffset(String text, int lineIndex)
	{
		int lineCount = 0;

		// 全ての文字に処理を行う。
		for (int index = 0; index < text.length(); index++)
		{
			if (lineCount == lineIndex)
			{
				return index;
			}

			// 文字を判定する。
			if (text.charAt(index) == '\n')
			{
				// 改行コードの場合、行数をインクリメントする。
				lineCount++;
			}
		}

		// 行数が指定された行より少ない場合、全体文字数を返却する。（今回の場合、ここに到達する事はない。）
		return text.length();
	}

	/**
	 * 行終了位置取得処理
	 * @param text テキスト
	 * @param lineIndex 行位置
	 * @return　行終了位置
	 */
	private int getLineEndOffset(String text, int lineIndex)
	{
		int lineCount = 0;

		// 全ての文字に処理を行う。
		for (int index = 0; index < text.length(); index++)
		{
			// 行位置・文字を判定する。
			if (lineCount == lineIndex && text.charAt(index) == '\n')
			{
				// 対象の行位置、かつ改行コードの場合、位置を返却する。
				return index;
			}

			// 文字を判定する。
			if (text.charAt(index) == '\n')
			{
				// 改行コードの場合、行数をインクリメントする。
				lineCount++;
			}
		}

		// 改行コードが存在しない場合、全体文字数を返却する。
		return text.length();
	}

	/**
	 * 全選択ハンドル
	 */
	@FXML
	private void handleSelectAll()
	{
		pathTextArea.selectAll();
	}

	/**
	 * 選択行のフォルダを開くハンドル
	 */
	@FXML
	private void handleOpenFolder()
	{
		if (pathTextArea.getText().isBlank())
		{
			return;
		}

		try
		{
			// 選択開始行位置・選択終了行位置を取得・保持する。
			int startLineIndex = pathTextArea.getText(0, pathTextArea.getSelection().getStart()).split("\n", -1).length - 1;
			int endLineIndex = pathTextArea.getText(0, pathTextArea.getSelection().getEnd()).split("\n", -1).length - 1;

			// パステキストエリアの空行も含め、全行取得する。
			String[] pathAr = pathTextArea.getText().split("\n", -1);
			List<String> openFolderPathList = new ArrayList<>();

			String path;

			// 表示フォルダリストを設定する。
			for (int index = startLineIndex; index <= endLineIndex; index++)
			{
				// パスを保持する。
				path = pathAr[index];

				// パスの有無を判定する。
				if (!path.isBlank())
				{
					// ドライブ文字を、大文字に変換し、保持する。
					path = path.substring(0, 1).toUpperCase() + path.substring(1);

					// パスの末尾を判定する。
					if ("\\".equals(path.substring(path.length() - 1)))
					{
						// 【\】の場合、削除する。
						path = path.substring(0, path.length() - 1);
					}

					// パスの有無を判定する。
					if (!openFolderPathList.contains(path))
					{
						// 未設定のパスの場合
						openFolderPathList.add(path);
					}
				}
			}

			// フォルダを開く。
			for (String openFolderPath : openFolderPathList)
			{
				File folder = new File(openFolderPath);
				if (folder.exists() && folder.isDirectory())
				{
					Desktop.getDesktop().open(folder);
				}
			}
		}
		catch (Exception e)
		{
			new CustomMessageBox().showMessageBox(
					primaryStage,
					Const.ERROR,
					e.getMessage(),
					AlertType.ERROR,
					Const.BUTTON_TEXT_LIST_OK_ONLY,
					0);
		}
	}
}

# FugyunFolderCreatorFX
ふぎゅんフォルダ作成（ＪａｖａＦＸ版）

◆メモ
作業フォルダパス：C:\Users\fugyu\Documents\TestJave2
対象Jarファイルパス：C:\Users\fugyu\Documents\TestJave2\FugyunFolderCreatorFX.jar
JREパス（JavaFXのため、別途ダウンロードしたもの）：C:\Users\fugyu\Documents\javafx-sdk-23.0.1\jmods
メインクラス：fugyunfoldercreatorfx.FugyunFolderCreatorFX
名称：FugyunFolderCreator
バージョン：1.0
アイコンパス：C:\Users\fugyu\OneDrive\デスクトップ\TestJave\Icon.ico
説明：説明
著作権：著作権
ベンダー：ベンダー
モジュールパス（Eclipseで出力したもの）：C:\Users\fugyu\OneDrive\ドキュメント\FugyunFolderCreatorFX_lib

◆やり方
１．Eclipseで実行可能なJarファイルを出力する。
２．jdepsで使用している依存モジュールを抽出し、jlinkで、最小のJREを生成（カスタムJRE）・実行可能なJarファイルを配置しているフォルダ内に配置する。
３．C#で、コンソールアプリケーションで、Jarファイルを起動するexeを作成する。（Delphi・VCLでも可能。下にやり方は記載。）
　　（一つのフォルダに、【実行可能なJarファイル】・【カスタムJRE】・【exe】が置いてる状態にする。）
４．exeを起動すると、Jarファイルが起動される。



◆Eclipseで実行可能なJarファイルの作成方法
１．メニューの【ファイル】→【エクスポート】を選択し、【エクスポート】ダイアログを表示する。
２．【エクスポート】ダイアログから、【Java】→【実行可能 JAR ファイル】を選択し、【次へ】ボタンを押下する。
３．【起動構成】・【エクスポート先】・【ライブラリー処理】を設定し、【完了】ボタンを押下する。
　　なお、JavaFXの場合、【ライブラリー処理】は、【生成される JARの隣のサブフォルダーに必須ライブラリーをコピー(C)】を選択し、その他はデフォルトで良い。


◆jdepsで、依存モジュールを確認する。
１．コマンドプロンプトを起動し、カレントディレクトリを、対象Jarファイルが存在しているフォルダにする。
２．以下のコマンドを実行し、結果を確認する。
（【FugyunFolderCreatorFX_lib】は、実行可能なJarファイルを作成時、Jarファイルと同時にEclipseから出力されたもの。
対象Jarファイル：FugyunFolderCreatorFX.jar）
コマンド：jdeps --module-path "C:\Users\fugyu\OneDrive\ドキュメント\FugyunFolderCreatorFX_lib" -s FugyunFolderCreatorFX.jar
コマンド（フルパスで指定しているもの）：jdeps --module-path "C:\Users\fugyu\OneDrive\ドキュメント\FugyunFolderCreatorFX_lib" -s "C:\Users\fugyu\OneDrive\ドキュメント\FugyunFolderCreatorFX.jar"
但し、【C:\Users\fugyu\Documents\TestJave2\FugyunFolderCreatorFX_lib】は、
【生成される JARの隣のサブフォルダーに必須ライブラリーをコピー(C)】で出力されたものを指定する。
結果の例：FugyunFolderCreatorFX.jar -> java.base
　　　　　FugyunFolderCreatorFX.jar -> java.desktop
　　　　　FugyunFolderCreatorFX.jar -> javafx.base
　　　　　FugyunFolderCreatorFX.jar -> javafx.controls
　　　　　FugyunFolderCreatorFX.jar -> javafx.fxml
　　　　　FugyunFolderCreatorFX.jar -> javafx.graphics


◆jlinkにて、カスタムJREを生成する。
１．JavaFx-sdkを入れてあるフォルダに、jmodsフォルダが存在しない場合、別途ダウンロードし（ファイル名の例：openjfx-23.0.1_windows-x64_bin-jmods.zip）、
【jmods】フォルダとその中に、各jmodsファイルを追加する。
ダウンロードＵＲＬ：https://jdk.java.net/javafx23/
のJMODsのWindows/x64を選択する。）

２．以下のコマンドを実行する。
　　jlink --module-path C:\Users\fugyu\Documents\javafx-sdk-23.0.1\jmods --add-modules java.base,java.desktop,javafx.base,javafx.controls,javafx.fxml,javafx.graphics --output runtime
　　【--module-path】は、パソコンに配置しているものを指定する。
　　あとは、【JavaFXを含まない場合】と同様


※【jmods】フォルダが入っているJDKは、JavaFXのものを指定する。


◆実行確認用コマンド
コマンド：C:\Users\fugyu\Documents\TestJave2\runtime\bin\java.exe -jar FugyunFolderCreatorFX.jar


◆うまく実行出来たコマンド（inputで指定したフォルダには、Jarファイルのみ配置）
※【name】に日本語を使用すると動かない。他のオプションに日本語を使用するのはＯＫ。
jpackage --input "C:\Users\fugyu\Documents\TestJave2\build" --name FugyunFolderCreator --main-jar FugyunFolderCreatorFX.jar --main-class fugyunfoldercreatorfx.FugyunFolderCreatorFX --type app-image --runtime-image "C:\Users\fugyu\Documents\TestJave2\runtime" --icon "C:\Users\fugyu\Documents\TestJave2\Icon.ico" --app-version 1.0 --copyright "Fugyun" --vendor "Fugyun" --verbose --dest "C:\Users\fugyu\Documents\TestJave2\Dist"
jpackage --input "C:\Users\fugyu\Documents\TestJave2\build" --name FugyunFolderCreator --main-jar FugyunFolderCreatorFX.jar --main-class fugyunfoldercreatorfx.FugyunFolderCreatorFX --type app-image --runtime-image "C:\Users\fugyu\Documents\TestJave2\runtime" --icon "C:\Users\fugyu\Documents\TestJave2\Icon.ico" --app-version 1.0 --copyright "ふぎゅん" --vendor "ふぎゅん" --verbose --dest "C:\Users\fugyu\Documents\TestJave2\Dist" --description "お狐さん"

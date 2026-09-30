package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト レポート機能
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// トップページURLにアクセス
		goTo("http://localhost:8080/lms");
		// ログイン画面が表示されるまで待つ
		visibilityTimeout(By.xpath("//h2[text()='ログイン']"), 5);
		// ログイン画面であることを検証
		assertEquals("ログイン", webDriver.findElement(By.xpath("//h2[text()='ログイン']")).getText());
		// エビデンス
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		//受生の　ログインIDとパスワードを入力する
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA02");
		webDriver.findElement(By.id("password")).sendKeys("StudentAA021");
		//ログインボタンを押下//
		webDriver.findElement(By.cssSelector("input[type='submit'][value='ログイン']")).click();

		// コース詳細画面であることを検証
		assertEquals("コース詳細", webDriver.findElement(By.cssSelector("ol.breadcrumb li.active")).getText());

		//エビデンス
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {

		// 「未提出」の行にある「詳細」ボタンをクリック
		WebElement detailButton = webDriver.findElement(By.xpath("//tr[td/span[text()='未提出']]//input[@value='詳細']"));
		//スクロールして画面全体
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", detailButton);

		visibilityTimeout(By.cssSelector("ol.breadcrumb li.active"), 5);
		//セクション詳細画面を検証
		assertEquals("セクション詳細", webDriver.findElement(By.cssSelector("ol.breadcrumb li.active")).getText());

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 日報を提出するボタンを押下し、レポート登録画面に遷移")
	void test04() {

		// 「日報を提出する」ボタンをクリック
		WebElement reportButton = webDriver
				.findElement(By.xpath("//input[@type='submit' and contains(@value,'日報') and contains(@value,'提出する')]"));
		reportButton.click();

		// レポート登録画面の入力欄が表示されるまで待つ
		visibilityTimeout(By.cssSelector("textarea[id^='content_']"), 5);

		// レポート登録画面が表示されたことを確認
		assertTrue(webDriver.findElement(By.cssSelector("textarea[id^='content_']")).isDisplayed());

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 内容を入力して「提出する」ボタンを押下し、セクション詳細画面のボタン名が更新される")
	void test05() {

		// 内容を入力
		webDriver.findElement(By.cssSelector("textarea[id^='content_']")).sendKeys("テスト　07　です。");

		// 「提出する」ボタンを押下
		webDriver.findElement(By.xpath("//button[text()='提出する']")).click();

		// セクション詳細画面に戻るまで待つ
		visibilityTimeout(By.cssSelector("ol.breadcrumb li.active"), 5);

		// セクション詳細画面であることを確認
		assertEquals("セクション詳細", webDriver.findElement(By.cssSelector("ol.breadcrumb li.active")).getText());

		// 提出後のボタンを取得
		WebElement reportButton = webDriver.findElement(
				By.xpath("//form[contains(@action,'/report/regist')]//input[@type='submit']"));

		// ボタン名を確認
		String buttonText = reportButton.getAttribute("value");

		assertTrue(buttonText.startsWith("提出済み") && buttonText.endsWith("を確認する"));

		getEvidence(new Object() {
		});
	}
}

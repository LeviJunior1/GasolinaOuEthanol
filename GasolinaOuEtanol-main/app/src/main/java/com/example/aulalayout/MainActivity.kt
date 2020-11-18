package com.example.aulalayout

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var imageSet: ImageView
    lateinit var btnCalc: Button
    lateinit var editTextGas: EditText
    lateinit var editTextEthanol: EditText
    private var textGas: Float = 0f
    private var textEthanol: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        setSupportActionBar(toolbar)

        editTextGas = findViewById(R.id.editTextGas)
        editTextEthanol = findViewById(R.id.editTextEthanol)
        btnCalc = findViewById(R.id.calc_btn)
        imageSet = findViewById(R.id.imageResult)

        btnCalc.setOnClickListener(View.OnClickListener {
            calcResult()
        })

        // Esse será o clique do botão no teclado virtual quando o metodo for done (Que pode ser alterado na activity com o atributo (android:imeOptions="actionDone"))
        editTextEthanol.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                calcResult()
                true
            }
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemView = item.itemId
        when (itemView) {
            R.id.refresh -> {
                cleanField()
                showToast(getString(R.string.refresh_msg))
            }
        }

        return false
    }

    private fun calcResult() {
        when {
            checkField() -> {
                // Aqui eu chamo o metodo para fechar o teclado virtual
                getView().hideKeyboard()
                cleanField()
                showToast(getString(R.string.error_field))
            }
            else -> {
                getView().hideKeyboard()
                setImage()
                textGas = 0f
                textEthanol = 0f
            }
        }
    }

    private fun cleanField() {
        editTextGas.text.clear()
        editTextEthanol.text.clear()
        textGas = 0f
        textEthanol = 0f
        imageSet.setImageResource(R.drawable.empty_dice)
    }

    private fun checkField(): Boolean {
        if (!checkFieldBlank()) {
            textGas = editTextGas.text.toString().toFloat()
            textEthanol = editTextEthanol.text.toString().toFloat()
        }
        return checkFieldNumber()
    }

    private fun checkFieldBlank(): Boolean {
        return editTextGas.text.toString().isBlank() || editTextEthanol.text.toString().isBlank()
    }

    private fun checkFieldNumber(): Boolean {
        return textGas < 1 && textEthanol < 1
    }

    private fun setImage() {
        when {
            bestGas() -> {
                imageSet.setImageResource(R.drawable.abasteca_etanol)
            }
            else -> {
                imageSet.setImageResource(R.drawable.abasteca_gasolina)
            }
        }
    }

    private fun bestGas(): Boolean {
        return (textEthanol / textGas) < 0.7
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    // Executa a função de tirar o teclado da tela (chamando uma view/activity como referencia)
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    // Encontra a view/activity atual que o usuário está no momento
    private fun getView(): View {
        return window.decorView.findViewById(android.R.id.content)
    }

}
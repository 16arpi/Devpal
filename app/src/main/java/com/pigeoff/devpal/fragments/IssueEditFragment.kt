package com.pigeoff.devpal.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.pigeoff.devpal.R
import com.pigeoff.devpal.database.mDatabase
import com.pigeoff.devpal.database.mDatabaseIssue
import com.pigeoff.devpal.util.UtilIssueUI
import com.pigeoff.devpal.util.UtilMain
import kotlinx.android.synthetic.main.fragment_edit_project.view.*

/**
 * A simple [Fragment] subclass.
 */
class IssueEditFragment(db: mDatabase, issueId: Int, issueParent: Int, issueType: Int) : Fragment() {

    /*
        Il est nécessaire de récupérer 4 variables
        - INTENT_EDIT_ACTION (Boolean) : détermine si on doit AJOUTER ou MODIFIER une ISSUE
        - INTENT_PARENT (Int) : détermine si le parent est un project (= 0) ou une issue (= 1)
        - INTENT_PARENT_ID : id du parent (pouvant être autant un projet qu'une issue, cf. ci-dessus)
        - INTENT_ISSUE_ID : id de l'issue à modifier SI et SEULEMENT SI ISSUE_EDIT_ACTION = true
     */

    private val db = db
    private val issueId: Int = issueId
    private val issueParent: Int = issueParent
    private val issueType: Int = issueType
    private var issue = mDatabaseIssue()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_issue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.toolbar2
        val editTitre = view.editTitre
        val editDescription = view.editDescription

        if (issueId == UtilMain().ISSUE_NO_ID) {
            issue.type = issueType
            issue.parent = issueParent
            issue.status = UtilIssueUI(issue).getStatusFromType(issueType)
            editTitre.requestFocus()
            Log.i("Status", issue.status.toString())
        }
        else {
            issue = db.issueDAO().getFromIssue(issueId)
            editTitre.setText(issue.title)
            editDescription.setText(issue.description)
        }

        toolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_confirm -> {
                    if (editTitre.text.toString().isNotEmpty()) {
                        if (issueId == UtilMain().ISSUE_NO_ID) {
                            issue.title = editTitre.text.toString()
                            issue.description = editDescription.text.toString()
                            db.issueDAO().addIssue(issue)
                        }
                        else {
                            issue.title = editTitre.text.toString()
                            issue.description = editDescription.text.toString()
                            db.issueDAO().updateIssue(issue)
                        }
                        requireActivity().finish()
                    }
                    else {
                        Snackbar.make(view, requireContext().getString(R.string.message_notitle), Snackbar.LENGTH_SHORT).show()
                    }

                }
                android.R.id.home -> {
                    requireActivity().finish()
                }
            }
            true
        }
    }


}

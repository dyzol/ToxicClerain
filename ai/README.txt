AI transcript: https://www.perplexity.ai/search/3ecfda68-43a9-4773-a916-e50975948e31

Model used: Perplexity.ai (for all AI interactions)

My question:
in intellij, when running a test with coverage, how do i see which lines are not being covered by the test (java)

AI response:
Short answer: Run your tests "with coverage", enable per-test tracking (if you want which test hit a line), then open the source file — uncovered lines are shown in the editor gutter and highlighted (red for uncovered, green for covered, yellow for partial).

Steps (IntelliJ IDEA):

Run tests with coverage: Right‑click the test / test folder / run configuration → "Run '<name>' with Coverage".

Open the source file you want to inspect; the coverage markers appear in the left gutter and the editor (green = covered, red = uncovered, yellow = partial).

If you need to know which individual tests touched a specific line, enable per‑test tracing: File → Settings → Build, Execution, Deployment → Coverage → enable "Track per test coverage" (or enable tracing in the Run/Debug configuration), then rerun coverage using the IntelliJ test runner (not the Gradle runner). After that you can use "Show tests covering line" on a line to list the test(s).

--
AI transcript https://www.perplexity.ai/search/61bad1d6-4591-490d-bbca-9e042c5225e9

My question:
why is stamp coupling better than data coupling sometimes, when and vice versa

Response:
Stamp coupling can be better than data coupling in some cases when you need to pass a whole object or record because it keeps the interface simpler, preserves meaning, and avoids a long list of parameters. It becomes especially useful when the receiving module naturally works with that structure, or when the structure may grow over time.

When stamp coupling is better
The caller and callee both naturally think in terms of the same object, such as User, Order, or Student data.

Passing many individual fields would make the function signature cluttered and harder to read.

The data structure is being used as a convenient bundle, and the receiver only needs a few fields right now, but the structure itself is still a useful abstraction.

When data coupling is better
The module only needs a small, fixed set of simple values, so passing just those values reduces dependency.

You want maximum modularity, easier testing, and less risk that unrelated changes to the structure will break the receiver.

You want to avoid unnecessary exposure of fields that the receiver does not need.

Why one may beat the other
Data coupling is generally the preferred design because it is looser and more maintainable. But stamp coupling can be the better practical choice when it improves clarity, keeps related data together, or avoids an awkward API with too many parameters.

--
AI transcript: https://www.perplexity.ai/search/ece80880-ddc9-453a-98f1-cf948a35bf8d

My prompt:
i've set up git on a project in intellij. i'm also signed in to my github account, how do i commit it to my account's empty newly created repository?

AI response:
You need two steps: make a local commit in IntelliJ, then push it to the remote GitHub repository you created. In IntelliJ, make sure the project’s Git remote points to your new empty repo, then use Commit and Push to send the files up.

What to do
Open your project in IntelliJ.

Go to Git > Manage Remotes.

Add your new GitHub repo URL as origin if it is not already there, or replace the old remote if one exists.

Open Commit in IntelliJ, select the files, write a commit message, and commit them locally.

Click Push so the commit goes to GitHub.

--
AI transcript: https://www.perplexity.ai/search/848a1d57-5c46-4f4c-a69e-8bccf1b0ce30

My prompt:
modules vs classes java

AI response:
In Java, a class is a blueprint for creating objects, while a module is a packaging and encapsulation unit for organizing packages and controlling what your code exposes to other code.
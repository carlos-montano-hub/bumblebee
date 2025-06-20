% Ildeberto de los Santos Ruiz
% ITTG, 2018
\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{Tesis}[2018/03/12]
\PassOptionsToPackage{T1}{fontenc}
\PassOptionsToPackage{utf8}{inputenc}
\PassOptionsToPackage{english,spanish,mexico,es-noindentfirst}{babel}
\PassOptionsToPackage{letterpaper,margin=2.5cm,left=3cm}{geometry}
\PassOptionsToPackage{small,bf}{caption}
\PassOptionsToPackage{colorlinks=true}{hyperref}
\PassOptionsToPackage{table,xcdraw}{xcolor}
\newcommand{\iTipoDoc}{Tesis:}
\newcommand{\iTipoAutor}{Estudiante:}
\newcommand{\iTipoReq}{como requisito para obtener el grado de}
\DeclareOption{informe}{
\renewcommand{\iTipoDoc}{\MakeUppercase{Informe de Avance}\\[0.25\baselineskip] que presenta}
\renewcommand{\iTipoReq}{como requisito parcial en el programa de}
}
\DeclareOption*{\PassOptionsToClass{\CurrentOption}{book}}
\ProcessOptions
\LoadClass[oneside,openany]{book}
\RequirePackage{fontenc}
\RequirePackage{inputenc}
\RequirePackage{microtype}
\RequirePackage{fourier}
\RequirePackage{babel}
\RequirePackage{geometry}
\RequirePackage{xcolor}
\RequirePackage{graphicx}
\RequirePackage{amsmath}
\RequirePackage{amssymb}
\RequirePackage{mathrsfs}
\RequirePackage{pifont}
\RequirePackage{caption}
\RequirePackage{subcaption}
\RequirePackage{siunitx}
\RequirePackage{fancyvrb}
\RequirePackage{fancyhdr}
\RequirePackage{hyperref}
\RequirePackage{multirow}
\RequirePackage{titling}
\RequirePackage{booktabs}
\RequirePackage{epigraph}
\RequirePackage{emptypage}
\RequirePackage{ifthen}
\RequirePackage{csquotes}
\RequirePackage[spanish]{babel}

\DisableLigatures[f]{encoding=*,family=*}
\setlength{\parskip}{\medskipamount}
\newcommand{\director}[1]{\newcommand{\thedirector}{#1}}
\newcommand{\codirector}[1]{\newcommand{\thecodirector}{#1}\newcommand*{\MYCODIRECTOR}{}}
\newcommand{\tutor}[1]{\newcommand{\thetutor}{#1}\newcommand*{\MYTUTOR}{}}
\newcommand{\grado}[1]{\newcommand{\thegrado}{#1}}
\newcommand{\autorizacion}[1]{\newcommand{\theautorizacion}{#1}}
\newcommand{\noskip}{\setlength{\parskip}{0em}}

\renewcommand{\maketitle}{
\begin{titlepage}
\pagenumbering{alph}\thispagestyle{empty}\noindent
\includegraphics[width=\textwidth]{assets/cabecera}

\begin{minipage}{0.9\textwidth}
\begin{center}
\null\bigskip\bigskip\bigskip
\Large\textbf{\thegrado}
\end{center}
\bigskip\bigskip
\begin{center}
\large{\iTipoDoc}\\[0.5\baselineskip]
\Large\textbf{\thetitle}\\[0.5\baselineskip]
\bigskip
\bigskip
\large{\iTipoAutor}\\[0.5\baselineskip]
\Large\textbf{\theauthor}
\end{center}
\begin{center} \large
\begin{tabular}{rl}
Director de tesis: & \textbf{\thedirector}
\ifdefined\MYCODIRECTOR
	\\ Codirector de tesis: & \textbf{\thecodirector} \\
\else
	\\
\fi
\ifdefined\MYTUTOR
	\smallskip
	Tutor: & \textbf{\thetutor}
\else
	\null
\fi
\end{tabular}
\end{center}
\bigskip\bigskip\bigskip
\begin{center}
Hermosillo, Sonora, México \\[0.5\baselineskip] \thedate
\end{center}
\end{minipage}
\ifthenelse{\boolean{showAutorizacion}}
{
    \thispagestyle{empty}
    \begin{center}
    \includegraphics[height=\textheight]{assets/autorizacion}
    \end{center}
}
{
    \null
}
\end{titlepage}
}

\newenvironment{dedicatoria}
{\cleardoublepage\null\vfill\thispagestyle{empty}}
{\vfill\vfill\null}
\newenvironment{agradecimientos}
{\cleardoublepage\null\vfill\thispagestyle{empty}}
{\vfill\null}
\normalsize
\endinput
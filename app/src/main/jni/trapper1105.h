/*
 * MATLAB Compiler: 4.2 (R14SP2)
 * Date: Mon Nov 03 10:48:39 2014
 * Arguments: "-B" "macro_default" "-t" "-l" "trapper1105.m" 
 */

#ifndef __trapper1105_h
#define __trapper1105_h 1

#if defined(__cplusplus) && !defined(mclmcr_h) && defined(__linux__)
#  pragma implementation "mclmcr.h"
#endif
#include "mclmcr.h"
#ifdef __cplusplus
extern "C" {
#endif

#if defined(__SUNPRO_CC)
/* Solaris shared libraries use __global, rather than mapfiles
 * to define the API exported from a shared library. __global is
 * only necessary when building the library -- files including
 * this header file to use the library do not need the __global
 * declaration; hence the EXPORTING_<library> logic.
 */

#ifdef EXPORTING_trapper1105
#define PUBLIC_trapper1105_C_API __global
#else
#define PUBLIC_trapper1105_C_API /* No import statement needed. */
#endif

#define LIB_trapper1105_C_API PUBLIC_trapper1105_C_API

#elif defined(_HPUX_SOURCE)

#ifdef EXPORTING_trapper1105
#define PUBLIC_trapper1105_C_API __declspec(dllexport)
#else
#define PUBLIC_trapper1105_C_API __declspec(dllimport)
#endif

#define LIB_trapper1105_C_API PUBLIC_trapper1105_C_API


#else

#define LIB_trapper1105_C_API

#endif

/* This symbol is defined in shared libraries. Define it here
 * (to nothing) in case this isn't a shared library. 
 */
#ifndef LIB_trapper1105_C_API 
#define LIB_trapper1105_C_API /* No special import/export declaration */
#endif

extern LIB_trapper1105_C_API 
bool trapper1105InitializeWithHandlers(mclOutputHandlerFcn error_handler,
                                       mclOutputHandlerFcn print_handler);

extern LIB_trapper1105_C_API 
bool trapper1105Initialize(void);

extern LIB_trapper1105_C_API 
void trapper1105Terminate(void);


extern LIB_trapper1105_C_API 
void mlxTrapper1105(int nlhs, mxArray *plhs[], int nrhs, mxArray *prhs[]);


extern LIB_trapper1105_C_API void mlfTrapper1105(int nargout, mxArray** y
                                                 , mxArray* x, mxArray* f1
                                                 , mxArray* f3, mxArray* fsl
                                                 , mxArray* fsh, mxArray* rp
                                                 , mxArray* rs, mxArray* Fs);

#ifdef __cplusplus
}
#endif

#endif
